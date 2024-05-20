package org.singing.app.ui.screens.record.audio

import androidx.compose.runtime.Stable
import com.singing.app.audio.createTrackAudioParser
import com.singing.app.audio.processAudioFile
import com.singing.audio.utils.ComposeFile
import com.singing.config.note.NotesStore
import com.singing.config.track.TrackProperties
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.singing.app.domain.repository.track.RecentTrackRepository
import org.singing.app.ui.base.AppViewModel
import org.singing.app.ui.screens.record.create.viewmodel.model.RecordItem
import org.singing.app.ui.screens.record.create.viewmodel.state.AudioProcessState

@Stable
class SelectAudioViewModel(
    recentTrackRepository: RecentTrackRepository,
) : AppViewModel() {
    private val _audioProcessState = MutableStateFlow<AudioProcessState?>(null)
    val audioProcessState = _audioProcessState.asStateFlow()

    private val _isRecentFavouriteTracksLoading = MutableStateFlow(true)
    val isRecentFavouriteTracksLoading = _isRecentFavouriteTracksLoading.asStateFlow()

    val recentFavouriteTracks =
        recentTrackRepository
            .getFavouriteTracks()
            .onEach {
                _isRecentFavouriteTracksLoading.value = false
            }
            .stateIn()

    fun resetAudioProcessState() {
        _audioProcessState.value = null
    }

    fun processAudio(inputFile: ComposeFile) {
        viewModelScope.launch {
            val audioFile = processAudioFile(inputFile)
                ?: throw IllegalArgumentException("Cannot process file: $inputFile")

            _audioProcessState.value = AudioProcessState(
                selectedAudio = audioFile,
                isParsing = true,
            )

            val parser = createTrackAudioParser(audioFile.file, TrackProperties.defaultFilters)

            val data = parser
                .parse()
                .map {
                    RecordItem(
                        note = NotesStore.findNote(it.frequency),
                        frequency = it.frequency,
                        time = it.positionMs,
                    )
                }
                .toList()

            _audioProcessState.value = AudioProcessState(
                selectedAudio = audioFile,
                data = data.toImmutableList(),
                isParsing = false,
            )
        }
    }
}
