package com.singing.app.domain.usecase

import androidx.paging.PagingData
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.repository.RecordRepository
import com.singing.domain.model.RecordPoint
import kotlinx.coroutines.flow.Flow

class GetRecordPointsUseCase(
    private val recordRepository: RecordRepository,
) {
    operator fun invoke(record: RecordData): Flow<PagingData<RecordPoint>> =
        recordRepository.getRecordPoints(record)
}
