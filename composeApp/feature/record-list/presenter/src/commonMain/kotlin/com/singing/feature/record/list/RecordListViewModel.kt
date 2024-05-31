package com.singing.feature.record.list

import androidx.paging.PagingData
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.Publication
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.model.valueOrNull
import com.singing.app.domain.provider.UserProvider
import com.singing.app.domain.usecase.DeleteRecordUseCase
import com.singing.app.domain.usecase.FindNoteUseCase
import com.singing.app.domain.usecase.FindRecordPublicationUseCase
import com.singing.app.domain.usecase.GetRecordPointsUseCase
import com.singing.app.domain.usecase.ListenRecordUpdatesUseCase
import com.singing.app.domain.usecase.PublishRecordUseCase
import com.singing.app.domain.usecase.UploadRecordUseCase
import com.singing.domain.model.RecordPoint
import com.singing.feature.record.list.domain.usecase.GetAnyRecordUseCase
import com.singing.feature.record.list.domain.usecase.GetRecordListUseCase
import com.singing.feature.record.list.viewmodel.RecordListIntent
import com.singing.feature.record.list.viewmodel.RecordListUiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class RecordListViewModel(
    initialRecordData: RecordData?,

    private val userProvider: UserProvider,
    private val getRecordPointsUseCase: GetRecordPointsUseCase,
    private val getRecordListUseCase: GetRecordListUseCase,
    private val getAnyRecordUseCase: GetAnyRecordUseCase,
    private val findNoteUseCase: FindNoteUseCase,
    private val uploadRecordUseCase: UploadRecordUseCase,
    private val publishRecordUseCase: PublishRecordUseCase,
    private val deleteRecordUseCase: DeleteRecordUseCase,
    private val findRecordPublicationUseCase: FindRecordPublicationUseCase,
    private val listenRecordUpdatesUseCase: ListenRecordUpdatesUseCase,
) : ScreenModel {
    private val _uiState = MutableStateFlow(
        RecordListUiState(
            selectedRecord = DataState.of(initialRecordData)
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _records = MutableStateFlow<PagingData<RecordData>>(PagingData.empty())
    val records = _records.asStateFlow()

    private val _recordPoints = MutableStateFlow<PagingData<RecordPoint>>(PagingData.empty())
    val recordPoints = _recordPoints.asStateFlow()

    private var listenSelectedRecordJob: Job? = null

    init {
        if (initialRecordData != null) {
            screenModelScope.launch {
                updateSelectedRecord(initialRecordData)
            }
        }

        screenModelScope.launch {
            userProvider.userFlow.collect { user ->
                _uiState.update { it.copy(user = user) }
            }
        }

        screenModelScope.launch {
            loadRecords()
        }
    }

    fun onIntent(intent: RecordListIntent) {
        screenModelScope.launch {
            when (intent) {
                is RecordListIntent.UpdateSelected -> {
                    updateSelectedRecord(intent.record)
                }

                is RecordListIntent.PublishRecord -> publishRecord(
                    intent.record,
                    intent.description
                )

                is RecordListIntent.UploadRecord -> uploadRecord(intent.record)

                is RecordListIntent.DeleteRecord -> deleteRecord(intent.record)
            }
        }
    }

    fun getNote(frequency: Double) = findNoteUseCase(frequency)

    suspend fun getRecordPublication(record: RecordData): Publication? {
        return findRecordPublicationUseCase(record)
    }

    private suspend fun loadRecords() {
        getRecordListUseCase()
            .cachedIn(screenModelScope)
            .onEach {
                if (uiState.value.selectedRecord is DataState.Empty) {
                    screenModelScope.launch {
                        val item = getAnyRecordUseCase()

                        if (item != null) {
                            updateSelectedRecord(item)
                        }
                    }
                }
            }
            .collect {
                _records.value = it
            }
    }

    @OptIn(FlowPreview::class)
    private fun updateSelectedRecord(record: RecordData) {
        if (uiState.value.selectedRecord.valueOrNull() == record) return

        listenSelectedRecordJob?.cancel()

        _uiState.update {
            it.copy(selectedRecord = DataState.Success(record))
        }

        listenSelectedRecordJob = screenModelScope.launch {
            launch {
                listenRecordUpdatesUseCase(record)
                    .sample(3000)
                    .collect { update ->
                        _uiState.update {
                            it.copy(selectedRecord = update)
                        }
                    }
            }

            launch {
                getRecordPointsUseCase(record)
                    .cachedIn(screenModelScope)
                    .collect {
                        _recordPoints.value = it
                    }
            }
        }
    }

    private suspend fun publishRecord(record: RecordData, description: String) {
        _uiState.update {
            it.copy(selectedRecord = DataState.Loading)
        }

        val update = publishRecordUseCase(record, description)

        _uiState.update {
            it.copy(selectedRecord = DataState.Success(update.record))
        }
    }

    private suspend fun uploadRecord(record: RecordData) {
        _uiState.update {
            it.copy(selectedRecord = DataState.Loading)
        }

        val update = uploadRecordUseCase(record)

        _uiState.update {
            it.copy(selectedRecord = DataState.Success(update))
        }
    }

    private suspend fun deleteRecord(record: RecordData) {
        _uiState.update {
            it.copy(selectedRecord = DataState.Loading)
        }

        deleteRecordUseCase(record)

        _uiState.update {
            it.copy(selectedRecord = DataState.Empty)
        }

        loadRecords()
    }
}
