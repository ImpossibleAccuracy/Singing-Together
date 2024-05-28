package com.singing.feature.record

import androidx.paging.PagingData
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.Publication
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.usecase.*
import com.singing.domain.model.RecordPoint
import com.singing.feature.record.domain.usecase.ListenRecordUpdatesUseCase
import com.singing.feature.record.viewmodel.RecordDetailIntent
import com.singing.feature.record.viewmodel.RecordDetailUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class RecordDetailViewModel(
    initialRecordData: RecordData,
    getRecordPointsUseCase: GetRecordPointsUseCase,
    private val listenRecordUpdatesUseCase: ListenRecordUpdatesUseCase,
    private val findNoteUseCase: FindNoteUseCase,
    private val uploadRecordUseCase: UploadRecordUseCase,
    private val publishRecordUseCase: PublishRecordUseCase,
    private val deleteRecordUseCase: DeleteRecordUseCase,
    private val findRecordPublicationUseCase: FindRecordPublicationUseCase
) : ScreenModel {
    val recordPoints: Flow<PagingData<RecordPoint>> = getRecordPointsUseCase(initialRecordData)

    private val _uiState = MutableStateFlow(RecordDetailUiState(DataState.Success(initialRecordData)))
    val uiState = _uiState.asStateFlow()

    private val record: RecordData
        get() {
            val recordState = uiState.value.record

            if (recordState !is DataState.Success) {
                throw IllegalStateException()
            }

            return recordState.data
        }

    init {
        screenModelScope.launch {
            listenRecordUpdatesUseCase(initialRecordData)
                .collect { update ->
                    _uiState.update {
                        it.copy(record = update)
                    }
                }
        }
    }

    fun onIntent(intent: RecordDetailIntent) {
        screenModelScope.launch {
            when (intent) {
                RecordDetailIntent.UploadRecord -> uploadRecordUseCase(record)

                is RecordDetailIntent.PublishRecord -> publishRecordUseCase(record, intent.description)

                RecordDetailIntent.DeleteRecord -> deleteRecordUseCase(record)
            }
        }
    }

    fun getNote(frequency: Double): String =
        findNoteUseCase(frequency)

    suspend fun getRecordPublication(): Publication? {
        return findRecordPublicationUseCase(record)
    }
}
