package org.singing.app.ui.screens.publication.details

import kotlinx.coroutines.launch
import org.singing.app.domain.model.RecordData
import org.singing.app.domain.model.RecordPoint
import org.singing.app.domain.player.RecordPlayer
import org.singing.app.domain.usecase.FindNoteUseCase
import org.singing.app.domain.usecase.GetRecordPointsUseCase
import org.singing.app.ui.base.AppViewModel

class PublicationDetailsViewModel(
    private val getRecordPointsUseCase: GetRecordPointsUseCase,
    private val findNoteUseCase: FindNoteUseCase,
    val recordPlayer: RecordPlayer,
) : AppViewModel() {
    override fun onDispose() {
        resetRecordPlayer()

        super.onDispose()
    }

    suspend fun getRecordPoints(record: RecordData): List<RecordPoint> =
        getRecordPointsUseCase(record)

    fun getNote(frequency: Double): String =
        findNoteUseCase(frequency)

    fun resetRecordPlayer() {
        viewModelScope.launch {
            recordPlayer.reset()
        }
    }
}
