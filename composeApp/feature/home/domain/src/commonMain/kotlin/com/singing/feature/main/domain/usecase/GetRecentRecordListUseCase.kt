package com.singing.feature.main.domain.usecase

import com.singing.app.domain.model.RecordData
import com.singing.app.domain.repository.RecordRepository
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetRecentRecordListUseCase(
    private val recordRepository: RecordRepository,
) {
    companion object {
        const val DEFAULT_LIMIT = 15
    }

    operator fun invoke(limit: Int = DEFAULT_LIMIT): Flow<PersistentList<RecordData>> =
        recordRepository.getRecentRecords()
            .map {
                if (it.size <= limit) it
                else it.subList(0, limit)
            }
            .map { it.toPersistentList() }
}