package com.singing.api.service.record

import com.singing.api.domain.model.Record
import java.io.File

interface RecordService {
    suspend fun getPublicRecords(
        onlyPublished: Boolean,
    ): List<Record>

    suspend fun getAccountRecords(accountId: Int): List<Record>

    suspend fun buildRecord(
        voiceFile: File,
        trackFile: File?,
    ): Record
}
