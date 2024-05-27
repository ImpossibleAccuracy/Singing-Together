package com.singing.app.domain.usecase

import com.singing.app.domain.model.RecordData
import com.singing.app.domain.repository.RecordRepository

class DeleteRecordUseCase(
    private val recordRepository: RecordRepository,
) {
    suspend operator fun invoke(record: RecordData) {
        recordRepository.deleteRecord(record)
    }
}
