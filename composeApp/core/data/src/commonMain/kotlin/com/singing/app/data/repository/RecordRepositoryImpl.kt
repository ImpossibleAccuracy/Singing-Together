package com.singing.app.data.repository

import androidx.paging.PagingData
import com.singing.app.base.ComposeFile
import com.singing.app.data.datasource.declaration.RecordDataSource
import com.singing.app.data.datasource.declaration.RecordFileDataSource
import com.singing.app.data.repository.base.PagingRepository
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.payload.RecordSaveData
import com.singing.app.domain.repository.RecordRepository
import com.singing.domain.model.RecordPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RecordRepositoryImpl(
    private val localDataSource: RecordDataSource.Local,
    private val remoteDataSource: RecordDataSource.Remote,
    private val recordFileDataSource: RecordFileDataSource,
) : RecordRepository, PagingRepository() {
    override suspend fun markPublished(record: RecordData) {
        if (record.isPublished) return

        localDataSource.markPublished(record)
    }

    override suspend fun saveRecord(
        data: RecordSaveData,
        saveRemote: Boolean,
    ): RecordData {
        // TODO: insert audio analyzer algorithm
//        localDataSource.saveRecord(data)

        if (saveRemote) {
//            remoteDataSource.uploadRecord()
        }

        TODO()
    }

    override suspend fun getAnyRecord(): RecordData? =
        !localDataSource.getAnyRecord() // TODO: add state handler

    override suspend fun listenRecordUpdates(recordData: RecordData): Flow<RecordData?> {
        if (!recordData.isSavedLocally) return flowOf(recordData)

        return localDataSource.listenRecordUpdates(recordData)
    }

    override fun getRecords(): Flow<PagingData<RecordData>> =
        doPagingRequest { page ->
            localDataSource.getRecords(page) // TODO: add fetch from remote
        }

    override fun getRecentRecords(): Flow<List<RecordData>> =
        localDataSource.getRecentRecords() // TODO: add fetch from remote

    override suspend fun uploadRecord(record: RecordData): RecordData {
        if (record.isSavedRemote) return record

        return !remoteDataSource.uploadRecord(record) // TODO: add state handler
    }

    override suspend fun deleteRecord(record: RecordData) {
        if (record.isSavedRemote) remoteDataSource.deleteRecord(record)
        if (record.isSavedLocally) localDataSource.deleteRecord(record)
    }

    override fun getRecordPoints(record: RecordData): Flow<PagingData<RecordPoint>> =
        doPagingRequest { page ->
            if (record.isSavedLocally) {
                localDataSource.getRecordPoints(page, record)
            } else {
                remoteDataSource.getRecordPoints(page, record)
            }
        }

    override suspend fun getRecordVoiceFile(record: RecordData): ComposeFile =
        !recordFileDataSource.getRecordVoiceFile(record) // TODO: add state handler

    override suspend fun getRecordTrackFile(record: RecordData): ComposeFile =
        !recordFileDataSource.getRecordTrackFile(record) // TODO: add state handler
}
