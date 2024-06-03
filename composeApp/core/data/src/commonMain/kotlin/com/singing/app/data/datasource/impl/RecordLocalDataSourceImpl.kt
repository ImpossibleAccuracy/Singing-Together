package com.singing.app.data.datasource.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneNotNull
import com.singing.app.audio.getFileDuration
import com.singing.app.base.ComposeFile
import com.singing.app.data.Constants
import com.singing.app.data.database.AppDatabase
import com.singing.app.data.datamapper.impl.map
import com.singing.app.data.datasource.declaration.RecordDataSource
import com.singing.app.data.datasource.declaration.RecordInfoDataSource
import com.singing.app.data.sqldelight.record.DocumentEntity
import com.singing.app.domain.model.RecordData
import com.singing.domain.model.RecordPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import pro.respawn.apiresult.ApiResult

class RecordLocalDataSourceImpl(
    private val appDatabase: AppDatabase,
    private val recordInfoDataSource: RecordInfoDataSource,
//    private val recordPointDataMapper: DataMapper<RecordItemEntity, RecordPoint>,
) : RecordDataSource.Local {
    override suspend fun getLocalIdByRemoteId(remoteId: Int): Int? =
        appDatabase.recordQueries
            .selectLocalIdByRemoteId(remoteId.toLong())
            .executeAsOneOrNull()
            ?.toInt()

    override suspend fun saveRecord(
        voiceFile: ComposeFile,
        trackFile: ComposeFile?,
        title: String?,
        remoteId: Int?,
        creatorId: Int?,
    ): ApiResult<RecordData> = ApiResult {
        val points = recordInfoDataSource.computeRecordPoints(voiceFile, trackFile)
        val accuracy = recordInfoDataSource.computeRecordAccuracy(points)
        val duration = getFileDuration(voiceFile)

        val voiceDocument = saveDocument(voiceFile)
        val trackDocument = trackFile?.let { saveDocument(it) }

        appDatabase.recordQueries.insert(
            remoteId = remoteId?.toLong(),
            createdAt = Clock.System.now().toString(),
            title = title,
            accuracy = accuracy,
            isPublished = 0,
            duration = duration,
            voiceRecordId = voiceDocument.id,
            trackId = trackDocument?.id,
            creatorId = creatorId?.toLong(),
        )

        val insertedId = appDatabase.recordQueries
            .selectLastInserted()
            .executeAsOne()

        val recordEntity = appDatabase.recordQueries
            .selectOne(insertedId.MAX!!)
            .executeAsOne()

        for (point in points) {
            appDatabase.recordItemQueries.insert(
                recordId = recordEntity.id,
                time = point.time,
                frequency = point.first,
                trackFrequency = point.second,
            )
        }

        map(recordEntity)
    }

    private fun saveDocument(file: ComposeFile): DocumentEntity {
        appDatabase.documentQueries.insert(
            Clock.System.now().toString(),
            file.fullPath,
        )

        val recordId = appDatabase.documentQueries
            .selectLastInserted()
            .executeAsOne()

        return appDatabase.documentQueries
            .selectOne(recordId.MAX!!)
            .executeAsOne()
    }

    override suspend fun markUploaded(
        record: RecordData,
        remoteId: Int,
    ): ApiResult<RecordData> = ApiResult {
        if (!record.isSavedLocally) throw IllegalArgumentException("Record not saved locally")

        appDatabase.recordQueries.updateRemoteId(remoteId.toLong(), record.key.localId!!.toLong())

        getRecord(record.key.localId!!)
    }

    override suspend fun markPublished(record: RecordData) {
        if (!record.isSavedLocally) throw IllegalArgumentException("Record not saved locally")

        appDatabase.recordQueries.updatePublished(1, record.key.localId!!.toLong())
    }

    private fun getRecord(recordId: Int): RecordData =
        appDatabase.recordQueries
            .selectOne(recordId.toLong())
            .executeAsOne()
            .let(::map)

    override suspend fun getRecords(page: Int): ApiResult<List<RecordData>> = ApiResult {
        appDatabase.recordQueries
            .selectPaginated(
                Constants.MAX_PAGE_SIZE.toLong(),
                Constants.MAX_PAGE_SIZE * page.toLong()
            )
            .executeAsList()
            .map(::map)
    }

    override fun getRecentRecords(): Flow<List<RecordData>> =
        appDatabase.recordQueries
            .selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { items ->
                items.map(::map)
                    .sortedByDescending { it.createdAt.instant }
            }

    override suspend fun getAnyRecord(): ApiResult<RecordData?> = ApiResult {
        val item = appDatabase.recordQueries
            .selectAny()
            .executeAsList()
            .firstOrNull()

        item?.let(::map)
    }

    override suspend fun listenRecordUpdates(record: RecordData): Flow<RecordData?> {
        if (!record.isSavedLocally) throw IllegalArgumentException("Record not saved locally")

        return appDatabase.recordQueries
            .selectOne(record.key.localId!!.toLong())
            .asFlow()
            .mapToOneNotNull(Dispatchers.IO)
            .filterNotNull()
            .map(::map)
    }

    override suspend fun deleteRecord(record: RecordData) {
        appDatabase.recordQueries.deleteOne(record.key.localId!!.toLong())
    }

    override suspend fun getRecordPoints(
        page: Int,
        record: RecordData
    ): ApiResult<List<RecordPoint>> = ApiResult {
        appDatabase.recordItemQueries
            .selectByRecordId(
                record.key.localId!!.toLong(),
                Constants.MAX_PAGE_SIZE.toLong(),
                Constants.MAX_PAGE_SIZE * page.toLong()
            )
            .executeAsList()
            .map(::map)
    }
}
