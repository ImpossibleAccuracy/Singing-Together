package org.singing.app.ui.screens.record.list

import com.singing.config.note.NotesStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.singing.app.domain.model.RecordData
import org.singing.app.domain.model.RecordPoint
import org.singing.app.domain.repository.record.RecordPlayer
import org.singing.app.domain.repository.record.RecordRepository
import org.singing.app.domain.store.account.UserContainer
import org.singing.app.domain.usecase.DeleteRecordUseCase
import org.singing.app.domain.usecase.PublishRecordUseCase
import org.singing.app.domain.usecase.UploadRecordUseCase
import org.singing.app.ui.base.AppViewModel

class RecordListViewModel(
    private val deleteRecordUseCase: DeleteRecordUseCase,
    private val publishRecordUseCase: PublishRecordUseCase,
    private val uploadRecordUseCase: UploadRecordUseCase,
    private val recordRepository: RecordRepository,
    val recordPlayer: RecordPlayer,
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

    private val _user = UserContainer.user
    val user = _user.asStateFlow()


    fun getNote(frequency: Double): String {
        return NotesStore.findNote(frequency)
    }

    suspend fun getRecordPoints(record: RecordData): List<RecordPoint> {
        return recordRepository.getRecordPoints(record)
    }

    fun resetRecordPlayer() =
        viewModelScope.launch {
            recordPlayer.stop()
            recordPlayer.setPosition(0)
        }

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
