package org.singing.app.ui.screens.record.details

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.singing.app.domain.model.Publication
import org.singing.app.domain.model.RecordData
import org.singing.app.domain.model.RecordPoint
import org.singing.app.domain.player.RecordPlayer
import org.singing.app.domain.repository.record.RecordRepository
import org.singing.app.domain.store.account.UserContainer
import org.singing.app.domain.usecase.*
import org.singing.app.ui.base.AppViewModel

class RecordDetailsViewModel(
    private val deleteRecordUseCase: DeleteRecordUseCase,
    private val publishRecordUseCase: PublishRecordUseCase,
    private val uploadRecordUseCase: UploadRecordUseCase,
    private val findRecordPublicationUseCase: FindRecordPublicationUseCase,
    private val getRecordPointsUseCase: GetRecordPointsUseCase,
    private val findNoteUseCase: FindNoteUseCase,
    val recordPlayer: RecordPlayer,
) : AppViewModel() {
    val selectedRecord = MutableStateFlow<RecordData?>(null)


    override fun onDispose() {
        resetRecordPlayer()
        super.onDispose()
    }


    fun getNote(frequency: Double): String =
        findNoteUseCase(frequency)

    fun resetRecordPlayer() {
        viewModelScope.launch {
            recordPlayer.reset()
        }
    }


    suspend fun getRecordPoints(record: RecordData): List<RecordPoint> =
        getRecordPointsUseCase(record)

    suspend fun getRecordPublication(record: RecordData): Publication =
        findRecordPublicationUseCase(record)

    fun uploadRecord(record: RecordData) =
        viewModelScope.launch {
            selectedRecord.value = uploadRecordUseCase(record)
        }

    fun publishRecord(record: RecordData, description: String) =
        viewModelScope.launch {
            publishRecordUseCase(record, description)
        }

    fun deleteRecord(record: RecordData) =
        viewModelScope.launch {
            deleteRecordUseCase(record)

            selectedRecord.value = null
        }
}
