package org.singing.app.ui.screens.record.details

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.singing.app.domain.model.Publication
import org.singing.app.domain.model.RecordData
import org.singing.app.domain.model.RecordPoint
import org.singing.app.domain.usecase.*
import org.singing.app.ui.base.AppViewModel

class RecordDetailsViewModel(
    private val deleteRecordUseCase: DeleteRecordUseCase,
    private val publishRecordUseCase: PublishRecordUseCase,
    private val uploadRecordUseCase: UploadRecordUseCase,
    private val findRecordPublicationUseCase: FindRecordPublicationUseCase,
    private val getRecordPointsUseCase: GetRecordPointsUseCase,
    private val findNoteUseCase: FindNoteUseCase,
) : AppViewModel() {
    val selectedRecord = MutableStateFlow<RecordData?>(null)


    fun getNote(frequency: Double): String =
        findNoteUseCase(frequency)

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
