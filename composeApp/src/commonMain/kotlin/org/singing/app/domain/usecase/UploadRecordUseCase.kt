package org.singing.app.domain.usecase

import org.singing.app.domain.model.RecordData
import org.singing.app.domain.repository.record.RecordRepository

class UploadRecordUseCase(
    private val recordRepository: RecordRepository,
) {
    suspend operator fun invoke(record: RecordData): RecordData {
        return recordRepository.uploadRecord(record)
    }
}
