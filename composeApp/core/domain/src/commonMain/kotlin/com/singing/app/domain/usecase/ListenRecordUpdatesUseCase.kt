package com.singing.app.domain.usecase

import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.RecordData
import kotlinx.coroutines.flow.Flow

class ListenRecordUpdatesUseCase {
    operator fun invoke(recordData: RecordData): Flow<DataState<RecordData>> = TODO()
}