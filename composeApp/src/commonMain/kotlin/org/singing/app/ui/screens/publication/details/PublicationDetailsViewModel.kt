package org.singing.app.ui.screens.publication.details

import org.singing.app.domain.model.RecordData
import org.singing.app.domain.model.RecordPoint
import org.singing.app.domain.usecase.FindNoteUseCase
import org.singing.app.domain.usecase.GetRecordPointsUseCase
import org.singing.app.ui.base.AppViewModel

class PublicationDetailsViewModel(
    private val getRecordPointsUseCase: GetRecordPointsUseCase,
    private val findNoteUseCase: FindNoteUseCase,
) : AppViewModel() {
    suspend fun getRecordPoints(record: RecordData): List<RecordPoint> =
        getRecordPointsUseCase(record)

    fun getNote(frequency: Double): String =
        findNoteUseCase(frequency)
}
