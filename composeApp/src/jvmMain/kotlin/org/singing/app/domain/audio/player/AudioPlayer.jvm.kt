package org.singing.app.domain.audio.player

import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.util.Duration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import org.singing.app.domain.model.audio.AudioFile

actual class AudioPlayer {
    companion object {
        private const val UPDATE_RATE = 100L
    }

    private var currentState: PlayerState = PlayerState.STOP

    private var player: MediaPlayer? = null

    actual suspend fun play(
        file: AudioFile,
        initPosition: Long,
    ): Flow<PlayerState> = callbackFlow {
        val media = Media(file.file.toURI().toString())
        player = MediaPlayer(media)

        with(player!!) {
            waitReady()
            seek(Duration.millis(initPosition.toDouble()))

            setOnPlaying {
                currentState = PlayerState.PLAY
                trySend(currentState)
            }

            setOnStopped {
                currentState = PlayerState.STOP
                trySend(currentState)
            }

            setOnEndOfMedia {
                currentState = PlayerState.STOP
                trySend(currentState)
            }

            setOnPaused {
                currentState = PlayerState.STOP
                trySend(currentState)
            }

            setOnStalled {
                currentState = PlayerState.STOP
                trySend(currentState)
            }

            play()
        }

        awaitClose {
            player?.stop()
            player?.dispose()
            player = null
        }
    }

    actual suspend fun stop() {
        player?.stop()
        player?.dispose()
        player = null

        currentState = PlayerState.STOP
    }

    actual suspend fun setPosition(position: Long) {
        player?.seek(Duration.millis(position.toDouble()))
    }

    actual fun createPositionFlow(): Flow<Long> {
        if (!isPlayingInternal()) throw IllegalStateException("Player not started")

        return flow {
            while (isPlaying()) {
                emit(player!!.currentTime.toMillis().toLong())

                delay(UPDATE_RATE)
            }
        }
    }

    actual suspend fun isPlaying(): Boolean {
        return isPlayingInternal()
    }

    private fun isPlayingInternal(): Boolean {
        return currentState == PlayerState.PLAY
    }
}
