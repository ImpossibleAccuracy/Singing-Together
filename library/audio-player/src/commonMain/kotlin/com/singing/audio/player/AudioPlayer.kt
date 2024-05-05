package com.singing.audio.player

import com.singing.audio.player.model.AudioFile
import kotlinx.coroutines.flow.Flow

expect class AudioPlayer() {
    // TODO: add prepare(file) fun

    /**
     * Start to play a new file
     * @return Flow with player states
     */
    suspend fun play(
        file: AudioFile,
        initPosition: Long,
    ): Flow<PlayerState>

    /**
     * Stop player
     */
    suspend fun stop()

    /**
     * Set playing position in microseconds.
     */
    suspend fun setPosition(position: Long)

    /**
     * Retrieve current playing position
     */
    fun createPositionFlow(): Flow<Long>

    /**
     * Checks if player is started
     */
    suspend fun isPlaying(): Boolean
}
