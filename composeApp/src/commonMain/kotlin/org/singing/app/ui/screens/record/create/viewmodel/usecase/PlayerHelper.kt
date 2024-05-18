package org.singing.app.ui.screens.record.create.viewmodel.usecase

import com.singing.audio.player.AudioPlayer
import com.singing.audio.player.PlayerState
import org.singing.app.domain.model.AudioFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PlayerHelper(
    private val coroutineScope: CoroutineScope,
    private val onPlayerStateUpdate: (PlayerState) -> Unit,
    private val onPlayerPositionUpdate: (Long) -> Unit,
) {
    private val player = AudioPlayer()

    fun startPlaying(
        track: AudioFile,
        initPosition: Long,
    ) {
        coroutineScope.launch {
            if (player.isPlaying()) {
                player.stop()
            }

            val totalDuration = track.duration

            val startPosition =
                if (initPosition >= totalDuration * 0.995) 0
                else initPosition

            player
                .play(track.file, startPosition)
                .onEach {
                    if (it == PlayerState.PLAY) {
                        startPlayerLoop()
                    }
                }
                .collect { playerState ->
                    onPlayerStateUpdate(playerState)
                }
        }
    }

    fun setPosition(newPosition: Long) {
        coroutineScope.launch {
            onPlayerPositionUpdate(newPosition)

            if (player.isPlaying()) {
                player.setPosition(newPosition)
            }
        }
    }

    fun stopPlaying() {
        coroutineScope.launch {
            if (player.isPlaying()) {
                player.stop()
            }
        }
    }

    private fun startPlayerLoop() {
        coroutineScope.launch {
            player.createPositionFlow()
                .collect { newPosition ->
                    onPlayerPositionUpdate(newPosition)
                }
        }
    }
}
