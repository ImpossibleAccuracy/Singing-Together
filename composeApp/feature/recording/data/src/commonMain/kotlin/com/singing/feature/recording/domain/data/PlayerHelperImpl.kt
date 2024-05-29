package com.singing.feature.recording.domain.data

import com.singing.audio.player.AudioPlayer
import com.singing.audio.player.PlayerState
import com.singing.domain.model.AudioFile
import com.singing.feature.recording.domain.PlayerHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlin.math.max

class PlayerHelperImpl : PlayerHelper {
    companion object {
        private const val RESTART_PERCENT_THRESHOLD = 0.005
        private const val MIN_RESTART_THRESHOLD = 100.0
    }

    private val player = AudioPlayer()

    private val _isPlaying = MutableStateFlow(false)
    override val isPlaying: Flow<Boolean> = _isPlaying.asStateFlow()

    private val _position = MutableStateFlow(0L)
    override val position: Flow<Long> = _position.asStateFlow()

    private var positionLooperJob: Job? = null

    override suspend fun startPlaying(track: AudioFile, initPosition: Long) {
        supervisorScope {
            if (player.isPlaying()) {
                player.stop()
            }

            val totalDuration = track.duration

            val startPosition = when ((totalDuration - initPosition) <= max(
                MIN_RESTART_THRESHOLD,
                totalDuration * RESTART_PERCENT_THRESHOLD,
            )) {
                true -> 0
                false -> initPosition
            }

            player
                .play(track.file, startPosition)
                .onEach {
                    positionLooperJob?.cancel()

                    positionLooperJob = launch {
                        if (it == PlayerState.PLAY) {
                            startPlayerLoop()
                        }

                        positionLooperJob = null
                    }
                }
                .collect { playerState ->
                    _isPlaying.value = playerState == PlayerState.PLAY
                }
        }
    }

    private suspend fun startPlayerLoop() {
        player.createPositionFlow()
            .collect { newPosition ->
                _position.value = newPosition
            }
    }

    override suspend fun setPosition(newPosition: Long) {
        _position.value = newPosition

        if (player.isPlaying()) {
            player.setPosition(newPosition)
        }
    }

    override suspend fun stopPlaying() {
        if (player.isPlaying()) {
            player.stop()
        }
    }
}