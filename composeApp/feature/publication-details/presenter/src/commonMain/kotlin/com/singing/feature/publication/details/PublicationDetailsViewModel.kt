package com.singing.feature.publication.details

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.Publication
import com.singing.app.domain.provider.UserProvider
import com.singing.app.domain.usecase.DeletePublicationUseCase
import com.singing.app.domain.usecase.FindNoteUseCase
import com.singing.app.domain.usecase.GetRecordPointsUseCase
import com.singing.feature.publication.details.viewmodel.PublicationDetailsIntent
import com.singing.feature.publication.details.viewmodel.PublicationDetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class PublicationDetailsViewModel(
    initialPublication: Publication,

    userProvider: UserProvider,
    getRecordPointsUseCase: GetRecordPointsUseCase,
    private val findNoteUseCase: FindNoteUseCase,
    private val deletePublicationUseCase: DeletePublicationUseCase,
) : ScreenModel {
    private val _uiState = MutableStateFlow(
        PublicationDetailsUiState(
            publication = DataState.of(initialPublication)
        )
    )
    val uiState = _uiState.asStateFlow()

    val recordPoints = getRecordPointsUseCase(initialPublication.record)

    init {
        screenModelScope.launch {
            userProvider.userFlow.collect { user ->
                _uiState.update { it.copy(user = user) }
            }
        }
    }

    fun onIntent(intent: PublicationDetailsIntent) {
        screenModelScope.launch {
            when (intent) {
                is PublicationDetailsIntent.DeletePublication -> {
                    deletePublicationUseCase(intent.publication)
                }
            }
        }
    }

    fun getNote(frequency: Double): String =
        findNoteUseCase(frequency)
}
