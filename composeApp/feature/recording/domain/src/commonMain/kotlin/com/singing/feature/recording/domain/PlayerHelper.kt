package com.singing.feature.recording.domain

import com.singing.domain.model.AudioFile
import kotlinx.coroutines.flow.Flow

interface PlayerHelper {
    val isPlaying: Flow<Boolean>
    val position: Flow<Long>

    suspend fun startPlaying(
        track: AudioFile,
        initPosition: Long,
    )

    suspend fun setPosition(newPosition: Long)

    suspend fun stopPlaying()
}
