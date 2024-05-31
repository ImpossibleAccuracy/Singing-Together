package com.singing.feature.recording.setup.usecase

import com.singing.app.domain.repository.RecentTrackRepository
import com.singing.domain.model.AudioFile

class AddRecentTrackUseCase(
    private val recentTrackRepository: RecentTrackRepository,
) {
    suspend operator fun invoke(file: AudioFile) {
        recentTrackRepository.addRecentTrack(file)
    }
}