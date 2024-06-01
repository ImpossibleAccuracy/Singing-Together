package com.singing.audio.player

import com.singing.app.base.ComposeFile
import com.singing.audio.waitReady
import javafx.scene.media.Media
import javafx.scene.media.MediaException
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
    private var prevFile: ComposeFile? = null

    actual suspend fun play(
        file: ComposeFile,
        initPosition: Long,
    ): Flow<PlayerState> = callbackFlow {
        lock.lock()

        if (prevFile != file) {
            player?.dispose()

            val media = try {
                Media(file.uri.toString())
            } catch (e: MediaException) {
                println("Error while playing ${file.uri}")
                throw e
            }

            player = MediaPlayer(media)
            player!!.waitReady()

            prevFile = file
        }

        player!!.seek(Duration.millis(initPosition.toDouble()))

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

        if (player == null || player?.status == MediaPlayer.Status.STOPPED) {
            lock.unlock()
            return
        }

        if (player!!.status != MediaPlayer.Status.STOPPED) {
            player?.pause()

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
