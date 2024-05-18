package org.singing.app.ui.screens.record.list

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.singing.app.domain.model.Publication
import org.singing.app.domain.model.RecordData
import org.singing.app.domain.model.RecordPoint
import org.singing.app.domain.repository.record.RecordRepository
import org.singing.app.domain.store.account.UserContainer
import org.singing.app.domain.usecase.*
import org.singing.app.ui.base.AppViewModel

class RecordListViewModel(
    private val deleteRecordUseCase: DeleteRecordUseCase,
    private val publishRecordUseCase: PublishRecordUseCase,
    private val uploadRecordUseCase: UploadRecordUseCase,
    private val findRecordPublicationUseCase: FindRecordPublicationUseCase,
    private val getRecordPointsUseCase: GetRecordPointsUseCase,
    private val findNoteUseCase: FindNoteUseCase,
    recordRepository: RecordRepository,
) : AppViewModel() {
    private val _isLoadingRecords = MutableStateFlow(true)
    val isLoadingRecords = _isLoadingRecords.asStateFlow()

    val records = recordRepository
        .getRecords()
        .onEach {
            _isLoadingRecords.value = false
        }
        .stateIn()

    val selectedRecord = MutableStateFlow(-1)

    val user = UserContainer.user.asStateFlow()


    fun getNote(frequency: Double): String =
        findNoteUseCase(frequency)

    suspend fun getRecordPoints(record: RecordData): List<RecordPoint> =
        getRecordPointsUseCase(record)

    suspend fun getRecordPublication(record: RecordData): Publication =
        findRecordPublicationUseCase(record)

    fun uploadRecord(record: RecordData) =
        viewModelScope.launch {
            val newRecord = uploadRecordUseCase(record)

            if (selectedRecord.value == records.value.indexOf(record)) {
                selectedRecord.value = records.value.indexOf(newRecord)
            }
        }

    fun publishRecord(record: RecordData, description: String) =
        viewModelScope.launch {
            publishRecordUseCase(record, description)
        }

    fun deleteRecord(record: RecordData) =
        viewModelScope.launch {
            deleteRecordUseCase(record)

            if (selectedRecord.value == records.value.indexOf(record)) {
                selectedRecord.value = records.value.indexOfFirst {
                    it != record
                }
            }
        }
}
