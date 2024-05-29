package com.singing.feature.recording.setup.usecase

import androidx.paging.PagingData
import com.singing.app.domain.model.RecentTrack
import com.singing.app.domain.repository.RecentTrackRepository
import kotlinx.coroutines.flow.Flow

class GetTrackListUseCase(
    private val trackRepository: RecentTrackRepository,
) {
    operator fun invoke(): Flow<PagingData<RecentTrack>> =
        trackRepository.getTracks()
}