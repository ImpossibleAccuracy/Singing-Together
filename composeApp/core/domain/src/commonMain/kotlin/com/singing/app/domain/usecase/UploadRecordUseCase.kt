package com.singing.app.domain.usecase

import com.singing.app.domain.model.RecordData
import com.singing.app.domain.repository.RecordRepository

class UploadRecordUseCase(
    private val recordRepository: RecordRepository,
) {
    suspend operator fun invoke(record: RecordData): RecordData {
        return recordRepository.uploadRecord(record)
    }
}
