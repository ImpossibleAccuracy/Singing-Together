package com.singing.feature.main.domain.usecase

import com.singing.app.domain.model.RecentTrack
import com.singing.app.domain.repository.RecentTrackRepository
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetRecentTracksUseCase(
    private val trackRepository: RecentTrackRepository,
) {
    companion object {
        const val DEFAULT_LIMIT = 15
    }

    operator fun invoke(limit: Int = DEFAULT_LIMIT): Flow<PersistentList<RecentTrack>> =
        trackRepository.getRecentTracks(limit)
            .map { it.toPersistentList() }
}
