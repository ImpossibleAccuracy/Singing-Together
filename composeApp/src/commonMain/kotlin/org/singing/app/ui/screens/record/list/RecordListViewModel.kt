package org.singing.app.ui.screens.record.list

import com.singing.config.note.NotesStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.singing.app.domain.model.RecordData
import org.singing.app.domain.model.RecordPoint
import org.singing.app.domain.repository.publication.PublicationRepository
import org.singing.app.domain.repository.record.RecordPlayer
import org.singing.app.domain.repository.record.RecordRepository
import org.singing.app.domain.store.account.UserContainer
import org.singing.app.ui.base.AppViewModel

class RecordListViewModel(
    private val publicationRepository: PublicationRepository,
    private val recordRepository: RecordRepository,
    val recordPlayer: RecordPlayer,
) : AppViewModel() {
    val records = recordRepository
        .getRecords()
        .stateIn()

    val selectedRecord = MutableStateFlow<RecordData?>(null)

    private val _user = UserContainer.user
    val user = _user.asStateFlow()


    init {
        viewModelScope.launch {
            val firstRecord = records
                .first { it.isNotEmpty() }
                .firstOrNull()

            if (selectedRecord.value == null) {
                selectedRecord.value = firstRecord
            }
        }
    }


    fun uploadRecord(record: RecordData) =
        viewModelScope.launch {
            val newRecord = recordRepository.uploadRecord(record)

            if (selectedRecord.value == record) {
                selectedRecord.value = newRecord
            }
        }

    fun deleteRecord(record: RecordData) =
        viewModelScope.launch {
            recordRepository.deleteRecord(record)

            if (record == selectedRecord.value) {
                selectedRecord.value = records.value.firstOrNull {
                    it != record
                }
            }
        }

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

    fun publishRecord(record: RecordData, description: String) =
        viewModelScope.launch {
            publicationRepository.publishRecord(record, description)
        }
}
