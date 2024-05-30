package com.singing.app.data.datasource.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneNotNull
import com.singing.app.base.ComposeFile
import com.singing.app.data.Constants
import com.singing.app.data.database.AppDatabase
import com.singing.app.data.datasource.declaration.RecordDataSource
import com.singing.app.data.datasource.utils.DataMapper
import com.singing.app.data.setup.file.FileStore
import com.singing.app.data.sqldelight.record.DocumentEntity
import com.singing.app.data.sqldelight.record.RecordEntity
import com.singing.app.data.sqldelight.record.RecordItemEntity
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.payload.RecordSaveData
import com.singing.domain.model.RecordPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import pro.respawn.apiresult.ApiResult
import java.io.ByteArrayInputStream

class RecordLocalDataSourceImpl(
    private val appDatabase: AppDatabase,
    private val fileStore: FileStore,
    private val recordDataMapper: DataMapper<RecordEntity, RecordData>,
    private val recordPointDataMapper: DataMapper<RecordItemEntity, RecordPoint>,
) : RecordDataSource.Local {
    override suspend fun saveRecord(
        data: RecordSaveData,
        remoteId: Long?,
        duration: Long,
        points: List<RecordPoint>
    ): ApiResult<RecordData> = ApiResult {
        val voiceFile = fileStore.storeFile(ByteArrayInputStream(data.record))
        val trackFile = data.track?.file?.let { fileStore.copyToStore(it) }

        val voiceDocument = saveDocument(voiceFile)
        val trackDocument = trackFile?.let { saveDocument(it) }

        appDatabase.recordQueries.insert(
            remoteId = remoteId,
            createdAt = Clock.System.now().toString(),
            title = data.title,
            isPublished = 0,
            duration = duration,
            voiceRecordId = voiceDocument.id,
            trackId = trackDocument?.id,
        )

        val recordEntity = appDatabase.recordQueries.selectLastInserted()
            .executeAsOne()

        for (point in points) {
            appDatabase.recordItemQueries.insert(
                recordId = recordEntity.id,
                time = point.time,
                frequency = point.first,
                trackFrequency = point.second,
            )
        }

        recordDataMapper.map(recordEntity)
    }

    private fun saveDocument(file: ComposeFile): DocumentEntity {
        appDatabase.documentQueries.insert(
            Clock.System.now().toString(),
            file.fullPath,
        )

        return appDatabase.documentQueries.selectLastInserted().executeAsOne()
    }

    override suspend fun markUploaded(
        record: RecordData,
        remoteId: Long
    ): ApiResult<RecordData> = ApiResult {
        if (!record.isSavedLocally) throw IllegalArgumentException("Record not saved locally")

        appDatabase.recordQueries.updateRemoteId(remoteId, record.key.localId!!.toLong())

        getRecord(record.key.localId!!)
    }

    override suspend fun markPublished(record: RecordData) {
        if (!record.isSavedLocally) throw IllegalArgumentException("Record not saved locally")

        appDatabase.recordQueries.updatePublished(1, record.key.localId!!.toLong())
    }

    private fun getRecord(recordId: Int): RecordData {
        val entity = appDatabase.recordQueries.selectOne(recordId.toLong()).executeAsOne()
        return recordDataMapper.map(entity)
    }

    override suspend fun getRecords(page: Int): ApiResult<List<RecordData>> = ApiResult {
        appDatabase.recordQueries
            .selectPaginated(
                Constants.MAX_PAGE_SIZE.toLong(),
                Constants.MAX_PAGE_SIZE * page.toLong()
            )
            .executeAsList()
            .map { recordDataMapper.map(it) }
    }

    override fun getRecentRecords(): Flow<List<RecordData>> =
        appDatabase.recordQueries
            .selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { items ->
                items.map(recordDataMapper::map)
                    .sortedByDescending { it.createdAt.instant }
            }

    override suspend fun getAnyRecord(): ApiResult<RecordData> = ApiResult {
        val item = appDatabase.recordQueries.selectAny().executeAsOne()

        recordDataMapper.map(item)
    }

    override suspend fun listenRecordUpdates(record: RecordData): Flow<RecordData?> {
        if (!record.isSavedLocally) throw IllegalArgumentException("Record not saved locally")

        return appDatabase.recordQueries
            .selectOne(record.key.localId!!.toLong())
            .asFlow()
            .mapToOneNotNull(Dispatchers.IO)
            .filterNotNull()
            .map { recordDataMapper.map(it) }
    }

    override suspend fun deleteRecord(record: RecordData) {
        if (!record.isSavedLocally) return

        appDatabase.recordQueries.deleteOne(record.key.localId!!.toLong())
    }

    override suspend fun getRecordPoints(
        page: Int,
        record: RecordData
    ): ApiResult<List<RecordPoint>> = ApiResult {
        if (!record.isSavedLocally) throw IllegalArgumentException("Record not saved locally")

        appDatabase.recordItemQueries
            .selectByRecordId(
                record.key.localId!!.toLong(),
                Constants.MAX_PAGE_SIZE.toLong(),
                Constants.MAX_PAGE_SIZE * page.toLong()
            )
            .executeAsList()
            .map(recordPointDataMapper::map)
    }
}
