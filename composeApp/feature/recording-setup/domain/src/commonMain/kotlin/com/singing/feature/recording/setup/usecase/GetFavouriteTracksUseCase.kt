package com.singing.feature.recording.setup.usecase

import com.singing.app.domain.model.RecentTrack
import com.singing.app.domain.repository.RecentTrackRepository
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFavouriteTracksUseCase(
    private val trackRepository: RecentTrackRepository,
) {
    companion object {
        private const val DEFAULT_LIMIT = 6
    }

    operator fun invoke(limit: Int = DEFAULT_LIMIT): Flow<PersistentList<RecentTrack>> =
        trackRepository.getFavouriteTracks(limit)
            .map { it.toPersistentList() }
}