package org.singing.app.domain.usecase

import org.singing.app.domain.model.RecordData
import org.singing.app.domain.repository.record.RecordRepository

class DeleteRecordUseCase(
    private val recordRepository: RecordRepository,
) {
    suspend operator fun invoke(record: RecordData) {
        recordRepository.deleteRecord(record)
    }
}
