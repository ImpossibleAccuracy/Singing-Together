package com.singing.feature.record.list

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.singing.app.domain.model.*
import com.singing.app.domain.provider.UserProvider
import com.singing.app.domain.usecase.*
import com.singing.domain.model.RecordPoint
import com.singing.feature.record.list.domain.usecase.GetAnyRecordUseCase
import com.singing.feature.record.list.domain.usecase.GetRecordListUseCase
import com.singing.feature.record.list.viewmodel.RecordListIntent
import com.singing.feature.record.list.viewmodel.RecordListUiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


private const val DisplayDelay = 100L

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
    private val deletedRecords = MutableStateFlow<List<RecordData>>(listOf())
    private val updatedRecords = MutableStateFlow<Map<ExtendedKey, RecordData>>(mapOf())

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
                RecordListIntent.ReloadRecords -> loadRecords()

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

    suspend fun getRecordPublication(record: RecordData): Publication {
        return findRecordPublicationUseCase(record)
    }

    private suspend fun loadRecords() {
        _records.value = PagingData.empty()

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
            .combine(deletedRecords) { paging, deletedRecords ->
                paging.filter {
                    !deletedRecords.contains(it)
                }
            }
            .combine(updatedRecords) { paging, updatedRecords ->
                paging.map { item ->
                    val key = updatedRecords.keys.firstOrNull {
                        (it.localId != null && it.localId == item.key.localId) ||
                                (it.remoteId != null && it.remoteId == item.key.remoteId)
                    }

                    if (key == null) item
                    else updatedRecords[key]!!
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
                    .sample(DisplayDelay)
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
            it.copy(selectedRecord = update)
        }

        updatedRecords.update {
            it.plus(record.key to record)
        }
    }

    private suspend fun deleteRecord(record: RecordData) {
        listenSelectedRecordJob?.cancel()

        _uiState.update {
            it.copy(selectedRecord = DataState.Loading)
        }

        deleteRecordUseCase(record)

        deletedRecords.update {
            it.plus(record)
        }

        _uiState.update {
            it.copy(selectedRecord = DataState.Empty)
        }
    }
}
