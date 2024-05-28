package com.singing.feature.main.domain.usecase

import com.singing.app.domain.model.RecentTrack
import com.singing.app.domain.repository.RecentTrackRepository

class UpdateTrackFavouriteUseCase(
    private val trackRepository: RecentTrackRepository,
) {
    suspend operator fun invoke(track: RecentTrack, isFavourite: Boolean) {
        if (track.isFavourite == isFavourite) return

        trackRepository.updateTrackFavourite(track, isFavourite)
    }
}