package com.singing.feature.recording.setup

import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.singing.app.base.ComposeFile
import com.singing.app.base.exists
import com.singing.feature.recording.setup.usecase.AddRecentTrackUseCase
import com.singing.feature.recording.setup.usecase.GetFavouriteTracksUseCase
import com.singing.feature.recording.setup.usecase.GetTrackListUseCase
import com.singing.feature.recording.setup.usecase.ParseAudioUseCase
import com.singing.feature.recording.setup.viewmodel.SelectAudioIntent
import com.singing.feature.recording.setup.viewmodel.SelectAudioUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class SelectAudioViewModel(
    private val parseAudioUseCase: ParseAudioUseCase,
    private val getFavouriteTracksUseCase: GetFavouriteTracksUseCase,
    private val addRecentTrackUseCase: AddRecentTrackUseCase,
    getTrackListUseCase: GetTrackListUseCase,
) : ScreenModel {
    private val _uiState = MutableStateFlow(SelectAudioUiState())
    val uiState = _uiState.asStateFlow()

    // TODO: add "select track" dialog
    val tracks = getTrackListUseCase().cachedIn(screenModelScope)

    init {
        screenModelScope.launch {
            getFavouriteTracksUseCase().collect { items ->
                _uiState.update {
                    it.copy(
                        tracks = items
                    )
                }
            }
        }
    }

    fun onIntent(intent: SelectAudioIntent) {
        when (intent) {
            SelectAudioIntent.ClearTrackData -> {
                _uiState.update { it.copy(trackData = null) }
            }

            is SelectAudioIntent.ProcessAudio -> {
                screenModelScope.launch(Dispatchers.Default) {
                    processAudio(intent.file)
                }
            }
        }
    }

    private suspend fun processAudio(inputFile: ComposeFile) {
        if (inputFile.exists) {
            _uiState.update { it.copy(isParsing = true) }

            val result = parseAudioUseCase(inputFile)

            addRecentTrackUseCase(result.selectedAudio)

            _uiState.update { it.copy(trackData = result, isParsing = false) }
        }
    }
}
