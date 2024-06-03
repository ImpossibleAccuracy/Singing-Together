package com.singing.app.data.repository

import androidx.paging.PagingData
import com.singing.app.base.ComposeFile
import com.singing.app.data.datasource.declaration.RecordDataSource
import com.singing.app.data.datasource.declaration.RecordFileDataSource
import com.singing.app.data.repository.base.PagingRepository
import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.payload.RecordSaveData
import com.singing.app.domain.provider.UserProvider
import com.singing.app.domain.provider.currentUser
import com.singing.app.domain.repository.RecordRepository
import com.singing.domain.model.RecordPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RecordRepositoryImpl(
    private val userProvider: UserProvider,
    private val localDataSource: RecordDataSource.Local,
    private val remoteDataSource: RecordDataSource.Remote,
    private val recordFileDataSource: RecordFileDataSource,
) : RecordRepository, PagingRepository() {
    override suspend fun markPublished(record: RecordData) {
        localDataSource.markPublished(record)
    }

    override suspend fun saveRecord(
        data: RecordSaveData,
        saveRemote: Boolean,
    ): DataState<RecordData> {
        val voiceFile = recordFileDataSource.storeVoiceFile(data)
        val trackFile = data.track?.let { recordFileDataSource.storeTrackFile(data) }

        val record = localDataSource
            .saveRecord(
                voiceFile = voiceFile,
                trackFile = trackFile,
                title = data.title,
                remoteId = null,
                creatorId = userProvider.currentUser?.id,
            )
            .asDataState

        return when {
            saveRemote && record is DataState.Success -> {
                val uploaded = remoteDataSource.uploadRecord(record.data).asDataState

                if (uploaded is DataState.Success) {
                    localDataSource
                        .markUploaded(record.data, uploaded.data.key.remoteId!!)
                        .asDataState
                } else {
                    uploaded
                }
            }

            else -> record
        }
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

    override suspend fun uploadRecord(record: RecordData): DataState<RecordData> {
        if (record.isSavedRemote) return DataState.of(record)

        val result = remoteDataSource.uploadRecord(record).asDataState

        if (result is DataState.Success) {
            localDataSource.markUploaded(record, result.data.key.remoteId!!)

            return result
        } else {
            return result
        }
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
