package com.singing.app.data.repository

import androidx.paging.PagingData
import com.singing.app.base.ComposeFile
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.payload.RecordSaveData
import com.singing.app.domain.repository.RecordRepository
import com.singing.domain.model.RecordPoint
import kotlinx.coroutines.flow.Flow

class RecordRepositoryImpl : RecordRepository, StateRepository<RecordData>() {
    override suspend fun saveRecord(
        data: RecordSaveData,
        saveRemote: Boolean,
    ): RecordData = TODO()

    override suspend fun markPublished(record: RecordData): RecordData = TODO()

    override fun getRecords(): Flow<List<RecordData>> = TODO()

    override suspend fun uploadRecord(record: RecordData): RecordData = TODO()

    override suspend fun deleteRecord(record: RecordData): Unit = TODO()

    override fun getRecordPoints(record: RecordData): Flow<PagingData<RecordPoint>> = TODO()

    override suspend fun getRecordVoiceFile(record: RecordData): ComposeFile = TODO()

    override suspend fun getRecordTrackFile(record: RecordData): ComposeFile = TODO()
}
