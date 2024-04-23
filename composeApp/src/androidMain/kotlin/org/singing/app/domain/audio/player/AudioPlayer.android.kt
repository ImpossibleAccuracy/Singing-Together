package org.singing.app.domain.audio.player

import kotlinx.coroutines.flow.Flow
import org.singing.app.domain.model.audio.AudioFile

actual class AudioPlayer {
    actual suspend fun play(
        file: AudioFile,
        initPosition: Long
    ): Flow<PlayerState> {
        TODO("Not yet implemented")
    }

    actual suspend fun stop() {
    }

    actual suspend fun setPosition(position: Long) {
    }

    actual fun createPositionFlow(): Flow<Long> {
        TODO("Not yet implemented")
    }

    actual suspend fun isPlaying(): Boolean {
        TODO("Not yet implemented")
    }
}
