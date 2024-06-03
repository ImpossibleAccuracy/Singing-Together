package com.singing.app.data.datasource.declaration

import com.singing.app.base.ComposeFile
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.payload.RecordSaveData
import pro.respawn.apiresult.ApiResult

interface RecordFileDataSource {
    suspend fun storeVoiceFile(data: RecordSaveData): ComposeFile

    suspend fun storeTrackFile(data: RecordSaveData): ComposeFile

    suspend fun getRecordVoiceFile(record: RecordData): ApiResult<ComposeFile>

    suspend fun getRecordTrackFile(record: RecordData): ApiResult<ComposeFile>
}