package com.singing.audio.player

import com.singing.audio.utils.ComposeFile
import kotlinx.coroutines.flow.Flow

actual class AudioPlayer actual constructor() {
    // TODO: add prepare(file) fun
    /**
     * Start to play a new file
     * @return Flow with player states
     */
    actual suspend fun play(
        file: ComposeFile,
        initPosition: Long
    ): Flow<PlayerState> {
        TODO("Not yet implemented")
    }

    /**
     * Stop player
     */
    actual suspend fun stop() {
        TODO("Not yet implemented")
    }

    /**
     * Set playing position in microseconds.
     */
    actual suspend fun setPosition(position: Long) {
        TODO("Not yet implemented")
    }

    /**
     * Retrieve current playing position
     */
    actual fun createPositionFlow(): Flow<Long> {
        TODO("Not yet implemented")
    }

    /**
     * Checks if player is started
     */
    actual suspend fun isPlaying(): Boolean {
        TODO("Not yet implemented")
    }

}