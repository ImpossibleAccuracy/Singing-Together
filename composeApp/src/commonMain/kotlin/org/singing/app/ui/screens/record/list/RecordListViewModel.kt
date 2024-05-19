package org.singing.app.ui.screens.record.list

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.singing.app.domain.model.Publication
import org.singing.app.domain.model.RecordData
import org.singing.app.domain.model.RecordPoint
import org.singing.app.domain.repository.record.RecordRepository
import org.singing.app.domain.store.account.UserContainer
import org.singing.app.domain.usecase.*
import org.singing.app.ui.base.AppViewModel

@Stable
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

            if (selectedRecord.value == null) {
                setSelectedRecord(it.firstOrNull())
            }
        }
        .stateIn()

    private val _selectedRecord = MutableStateFlow<RecordData?>(null)
    val selectedRecord = _selectedRecord.asStateFlow()

    val user = UserContainer.user.asStateFlow()


    fun setSelectedRecord(record: RecordData?) {
        _selectedRecord.value = record
    }

    fun getNote(frequency: Double): String =
        findNoteUseCase(frequency)

    suspend fun getRecordPoints(record: RecordData): List<RecordPoint> =
        getRecordPointsUseCase(record)

    suspend fun getRecordPublication(record: RecordData): Publication =
        findRecordPublicationUseCase(record)

    fun uploadRecord(record: RecordData) =
        viewModelScope.launch {
            val newRecord = uploadRecordUseCase(record)

            if (selectedRecord.value == record) {
                setSelectedRecord(newRecord)
            }
        }

    fun publishRecord(record: RecordData, description: String) =
        viewModelScope.launch {
            val recordIndex = records.value.indexOf(record)

            publishRecordUseCase(record, description)

            val updated = records.first()

            setSelectedRecord(updated[recordIndex])
        }

    fun deleteRecord(record: RecordData) =
        viewModelScope.launch {
            deleteRecordUseCase(record)

            if (selectedRecord.value == record) {
                setSelectedRecord(
                    records.value.firstOrNull {
                        it != record
                    }
                )
            }
        }
}
