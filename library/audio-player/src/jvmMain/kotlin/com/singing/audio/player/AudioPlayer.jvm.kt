package com.singing.audio.player

import com.singing.app.base.ComposeFile
import com.singing.audio.waitReady
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.util.Duration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex

actual class AudioPlayer {
    companion object {
        private const val UPDATE_RATE = 100L
    }

    private val lock = Mutex()

    private var currentState: PlayerState = PlayerState.STOP

    private var player: MediaPlayer? = null

    actual suspend fun play(
        file: ComposeFile,
        initPosition: Long,
    ): Flow<PlayerState> = callbackFlow {
        val media = Media(file.uri.toString())

        player = MediaPlayer(media)
        player!!.waitReady()
        player!!.seek(Duration.millis(initPosition.toDouble()))

        lock.lock()

        val localPlayer = player ?: return@callbackFlow

        localPlayer.setOnPlaying {
            currentState = PlayerState.PLAY
            trySend(currentState)
        }

        localPlayer.setOnStopped {
            currentState = PlayerState.STOP
            trySend(currentState)
        }

        localPlayer.setOnEndOfMedia {
            currentState = PlayerState.STOP
            trySend(currentState)
        }

        localPlayer.setOnPaused {
            currentState = PlayerState.STOP
            trySend(currentState)
        }

        localPlayer.setOnStalled {
            currentState = PlayerState.STOP
            trySend(currentState)
        }

        localPlayer.setOnError {
            println(player!!.error)

            throw player!!.error
        }

        localPlayer.play()

        lock.unlock()

        awaitClose {
            try {
                player?.stop()
                player?.dispose()
            } catch (e: NullPointerException) {
                System.err.println(e)
                e.printStackTrace(System.err)
            }

            player = null
        }
    }

    actual suspend fun stop() {
        if (player == null) return

        lock.lock()

        if (player == null) {
            lock.unlock()
            return
        }

        if (player!!.status != MediaPlayer.Status.DISPOSED) {
            player?.stop()
            player?.dispose() // TODO: dispose only on next play
            player = null

            currentState = PlayerState.STOP
        }

        lock.unlock()
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
