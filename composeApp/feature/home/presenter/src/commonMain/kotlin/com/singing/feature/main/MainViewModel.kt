package com.singing.feature.main

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.singing.app.domain.model.Publication
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.provider.UserProvider
import com.singing.app.domain.usecase.DeleteRecordUseCase
import com.singing.app.domain.usecase.FindRecordPublicationUseCase
import com.singing.app.domain.usecase.PublishRecordUseCase
import com.singing.app.domain.usecase.UploadRecordUseCase
import com.singing.feature.main.domain.usecase.GetRecentPublicationsUseCase
import com.singing.feature.main.domain.usecase.GetRecentRecordUseCase
import com.singing.feature.main.domain.usecase.GetRecentTracksUseCase
import com.singing.feature.main.domain.usecase.UpdateTrackFavouriteUseCase
import com.singing.feature.main.viewmodel.MainIntent
import com.singing.feature.main.viewmodel.MainUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class MainViewModel(
    getRecentRecordUseCase: GetRecentRecordUseCase,
    getRecentTracksUseCase: GetRecentTracksUseCase,
    getRecentPublicationsUseCase: GetRecentPublicationsUseCase,

    private val uploadRecordUseCase: UploadRecordUseCase,
    private val publishRecordUseCase: PublishRecordUseCase,
    private val deleteRecordUseCase: DeleteRecordUseCase,
    private val updateTrackFavouriteUseCase: UpdateTrackFavouriteUseCase,
    private val findRecordPublicationUseCase: FindRecordPublicationUseCase,
    private val userProvider: UserProvider,
) : ScreenModel {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    init {
        screenModelScope.launch {
            combine(
                userProvider.userFlow,
                getRecentRecordUseCase(),
                getRecentTracksUseCase(),
                getRecentPublicationsUseCase()
            ) { user, records, tracks, publications ->
                _uiState.update {
                    it.copy(
                        user = user,
                        records = records,
                        recentTracks = tracks,
                        latestPublications = publications,
                    )
                }
            }.collect()
        }
    }

    fun onIntent(intent: MainIntent) {
        screenModelScope.launch {
            when (intent) {
                is MainIntent.UploadRecord -> uploadRecordUseCase(intent.record)
                is MainIntent.DeleteRecord -> deleteRecordUseCase(intent.record)
                is MainIntent.PublishRecord -> publishRecordUseCase(intent.record, intent.description)
                is MainIntent.UpdateTrackFavourite -> updateTrackFavouriteUseCase(intent.track, intent.isFavourite)
            }
        }
    }

    suspend fun getRecordPublication(record: RecordData): Publication? =
        findRecordPublicationUseCase(record)
}
