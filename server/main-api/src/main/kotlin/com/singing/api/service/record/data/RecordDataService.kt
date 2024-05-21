package com.singing.api.service.record.data

import com.singing.api.domain.model.DocumentEntity
import com.singing.api.domain.model.RecordEntity
import com.singing.api.domain.model.RecordItemEntity
import java.io.File
import java.util.*

interface RecordDataService {
    suspend fun buildRecord(
        voiceFile: File,
        trackFile: File?,
    ): RecordEntity

    suspend fun recordPoints(recordId: Int): List<RecordItemEntity>

    suspend fun loadRecordVoiceFile(recordId: Int): DocumentEntity

    suspend fun loadRecordTrackFile(recordId: Int): Optional<DocumentEntity>
}
