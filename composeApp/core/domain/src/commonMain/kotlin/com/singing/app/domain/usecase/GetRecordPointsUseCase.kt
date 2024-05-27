package com.singing.app.domain.usecase

import com.singing.app.domain.model.RecordData
import com.singing.app.domain.repository.RecordRepository
import com.singing.domain.model.RecordPoint

class GetRecordPointsUseCase(
    private val recordRepository: RecordRepository,
) {
    suspend operator fun invoke(record: RecordData): List<RecordPoint> =
        recordRepository.getRecordPoints(record)
}
