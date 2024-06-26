package com.singing.app.data.datasource.declaration

import com.singing.app.base.ComposeFile
import com.singing.app.domain.model.RecordData
import com.singing.domain.model.RecordPoint
import kotlinx.coroutines.flow.Flow
import pro.respawn.apiresult.ApiResult

sealed interface RecordDataSource {
    suspend fun deleteRecord(record: RecordData)

    suspend fun getRecordPoints(page: Int, record: RecordData): ApiResult<List<RecordPoint>>

    interface Local : RecordDataSource {
        suspend fun getLocalIdByRemoteId(remoteId: Int): Int?

        suspend fun saveRecord(
            voiceFile: ComposeFile,
            trackFile: ComposeFile?,
            title: String?,
            remoteId: Int?,
            creatorId: Int?,
        ): ApiResult<RecordData>

        suspend fun markUploaded(record: RecordData, remoteId: Int): ApiResult<RecordData>

        suspend fun markPublished(record: RecordData, published: Boolean)

        suspend fun getRecords(page: Int): ApiResult<List<RecordData>>

        fun getRecentRecords(): Flow<List<RecordData>>

        suspend fun getAnyRecord(): ApiResult<RecordData?>

        suspend fun listenRecordUpdates(record: RecordData): Flow<RecordData?>
    }

    interface Remote : RecordDataSource {
        suspend fun uploadRecord(record: RecordData): ApiResult<RecordData>
    }
}