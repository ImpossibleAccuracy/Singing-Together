package com.singing.feature.main

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.singing.app.domain.model.Publication
import com.singing.app.domain.provider.UserProvider
import com.singing.app.domain.usecase.FindNoteUseCase
import com.singing.app.domain.usecase.GetRecordPointsUseCase
import com.singing.feature.main.viewmodel.PublicationDetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class PublicationDetailsViewModel(
    initialPublication: Publication,

    userProvider: UserProvider,
    getRecordPointsUseCase: GetRecordPointsUseCase,
    private val findNoteUseCase: FindNoteUseCase,
) : ScreenModel {
    private val _uiState = MutableStateFlow(
        PublicationDetailsUiState(
            publication = initialPublication
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        screenModelScope.launch {
            userProvider.userFlow.collect { user ->
                _uiState.update { it.copy(user = user) }
            }
        }
    }

    val recordPoints = getRecordPointsUseCase(initialPublication.record)

    fun getNote(frequency: Double): String =
        findNoteUseCase(frequency)
}
