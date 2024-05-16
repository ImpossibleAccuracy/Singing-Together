package org.singing.app.domain.usecase

import org.singing.app.domain.model.RecordData
import org.singing.app.domain.model.RecordPoint
import org.singing.app.domain.repository.record.RecordRepository

class GetRecordPointsUseCase(
    private val recordRepository: RecordRepository,
) {
    suspend operator fun invoke(record: RecordData): List<RecordPoint> =
        recordRepository.getRecordPoints(record)
}
