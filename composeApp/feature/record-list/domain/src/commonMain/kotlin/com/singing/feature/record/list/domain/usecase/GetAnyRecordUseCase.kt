package com.singing.feature.record.list.domain.usecase

import com.singing.app.domain.repository.RecordRepository

class GetAnyRecordUseCase(
    private val recordRepository: RecordRepository,
) {
    suspend operator fun invoke() =
        recordRepository.getAnyRecord()
}