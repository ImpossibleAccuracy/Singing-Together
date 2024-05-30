package com.singing.app.domain.usecase

import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.repository.RecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class ListenRecordUpdatesUseCase(
    private val recordRepository: RecordRepository,
) {
    operator fun invoke(recordData: RecordData): Flow<DataState<RecordData>> = flow {
        emit(DataState.Loading)

        recordRepository.listenRecordUpdates(recordData)
            .catch {
                DataState.Error(it.message ?: "Error")
            }
            .collect {
                DataState.of(it)
            }
    }
}