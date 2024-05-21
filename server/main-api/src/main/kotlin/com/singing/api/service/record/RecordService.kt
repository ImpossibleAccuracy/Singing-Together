package com.singing.api.service.record

import com.singing.api.domain.model.RecordEntity
import com.singing.api.domain.model.RecordItemEntity
import java.io.File
import java.util.*

interface RecordService {
    suspend fun save(record: RecordEntity): RecordEntity

    suspend fun get(recordId: Int): Optional<RecordEntity>

    suspend fun publicRecords(onlyPublished: Boolean): List<RecordEntity>

    suspend fun accountRecords(accountId: Int): List<RecordEntity>

    suspend fun delete(recordId: Int)
}
