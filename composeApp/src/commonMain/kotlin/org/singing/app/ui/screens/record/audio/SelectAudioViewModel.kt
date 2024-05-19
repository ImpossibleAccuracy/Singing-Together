package org.singing.app.ui.screens.record.audio

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import org.singing.app.domain.repository.track.RecentTrackRepository
import org.singing.app.ui.base.AppViewModel

@Stable
class SelectAudioViewModel(
    recentTrackRepository: RecentTrackRepository,
) : AppViewModel() {
    private val _isRecentFavouriteTracksLoading = MutableStateFlow(true)
    val isRecentFavouriteTracksLoading = _isRecentFavouriteTracksLoading.asStateFlow()

    val recentFavouriteTracks =
        recentTrackRepository
            .getFavouriteTracks()
            .onEach {
                _isRecentFavouriteTracksLoading.value = false
            }
            .stateIn()
}
