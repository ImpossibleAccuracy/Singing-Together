package com.singing.app.domain.repository

import androidx.paging.PagingData
import com.singing.app.base.ComposeFile
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.payload.RecordSaveData
import com.singing.domain.model.RecordPoint
import kotlinx.coroutines.flow.Flow

interface RecordRepository {
    suspend fun saveRecord(data: RecordSaveData, saveRemote: Boolean): RecordData

    fun getRecords(): Flow<List<RecordData>>

    suspend fun markPublished(record: RecordData): RecordData

    suspend fun uploadRecord(record: RecordData): RecordData

    suspend fun deleteRecord(record: RecordData)

    fun getRecordPoints(record: RecordData): Flow<PagingData<RecordPoint>>

    suspend fun getRecordVoiceFile(record: RecordData): ComposeFile

    suspend fun getRecordTrackFile(record: RecordData): ComposeFile
}
