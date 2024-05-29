package com.singing.feature.main

import cafe.adriel.voyager.core.model.ScreenModel
import com.singing.app.domain.model.Publication
import com.singing.app.domain.usecase.FindNoteUseCase
import com.singing.app.domain.usecase.GetRecordPointsUseCase
import com.singing.feature.main.viewmodel.PublicationDetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class PublicationDetailsViewModel(
    initialPublication: Publication,

    getRecordPointsUseCase: GetRecordPointsUseCase,
    private val findNoteUseCase: FindNoteUseCase,
) : ScreenModel {
    private val _uiState = MutableStateFlow(
        PublicationDetailsUiState(
            publication = initialPublication
        )
    )
    val uiState = _uiState.asStateFlow()

    val recordPoints = getRecordPointsUseCase(initialPublication.record)

    fun getNote(frequency: Double): String =
        findNoteUseCase(frequency)
}
