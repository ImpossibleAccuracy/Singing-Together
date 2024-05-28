package com.singing.feature.record.list.domain.usecase

import androidx.paging.PagingData
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.repository.RecordRepository
import kotlinx.coroutines.flow.Flow

class GetRecordListUseCase(
    private val recordRepository: RecordRepository,
) {
    operator fun invoke(): Flow<PagingData<RecordData>> = recordRepository.getRecords()
}