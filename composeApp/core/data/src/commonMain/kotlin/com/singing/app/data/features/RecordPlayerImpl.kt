package com.singing.app.data.features

import com.singing.app.base.ComposeFile
import com.singing.app.domain.features.RecordPlayer
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.repository.RecordRepository
import com.singing.audio.player.AudioPlayer
import com.singing.audio.player.PlayerState
import com.singing.audio.player.multiplyPlayers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.math.max


class RecordPlayerImpl(private val recordRepository: RecordRepository) : RecordPlayer {
    companion object {
        private const val POSITION_UPDATE_RATE = 300L
        private const val RESTART_PERCENT_THRESHOLD = 0.005
        private const val MIN_RESTART_THRESHOLD = 100.0
    }


    private val recorderScope = CoroutineScope(Dispatchers.Default)

    private val _isPlaying = MutableStateFlow(false)
    override val isPlaying = _isPlaying.asStateFlow()

    private val _position = MutableStateFlow<Long>(0)
    override val position = _position.asStateFlow()

    private var positionLoopJob: Job? = null
    private val player1 = AudioPlayer()
    private val player2 = AudioPlayer()


    override suspend fun play(recordData: RecordData) {
        val voiceFile = recordRepository.getRecordVoiceFile(recordData)

        if (recordData.duration - position.value <= max(
                MIN_RESTART_THRESHOLD,
                recordData.duration * RESTART_PERCENT_THRESHOLD,
            )
        ) {
            _position.value = 0
        }

        when (recordData) {
            is RecordData.Vocal -> playSingle(
                voiceFile = voiceFile
            )

            is RecordData.Cover -> playWithTrack(
                voiceFile = voiceFile,
                trackFile = recordRepository.getRecordTrackFile(recordData)
            )
        }
    }

    private suspend fun playSingle(voiceFile: ComposeFile) {
        player1
            .play(voiceFile, position.value)
            .distinctUntilChanged()
            .onEach {
                if (it == PlayerState.PLAY) {
                    startPlayerLoop()
                }
            }
            .collect { playerState ->
                _isPlaying.value = playerState == PlayerState.PLAY
            }
    }

    private suspend fun playWithTrack(voiceFile: ComposeFile, trackFile: ComposeFile) =
        coroutineScope {
            val result = multiplyPlayers(
                player1, player2,
                { it.play(voiceFile, position.value) },
                { it.play(trackFile, position.value) }
            )

            result
                .distinctUntilChanged()
                .collect { playerState ->
                    if (playerState == PlayerState.PLAY) {
                        startPlayerLoop()
                    }

                    _isPlaying.value = playerState == PlayerState.PLAY
                }
        }

    private suspend fun startPlayerLoop() {
        positionLoopJob?.cancel()

        positionLoopJob = recorderScope.launch {
            player1.createPositionFlow().collectLatest { newPosition ->
                _position.value = newPosition

                delay(POSITION_UPDATE_RATE)
            }

            positionLoopJob = null
        }
    }

    override suspend fun reset() {
        positionLoopJob?.cancel()

        stop()
        setPosition(0)
    }

    override suspend fun setPosition(newPosition: Long) {
        player1.setPosition(newPosition)
        player2.setPosition(newPosition)

        _position.value = newPosition
    }

    override suspend fun stop() {
        _isPlaying.value = false

        player1.stop()
        player2.stop()
    }
}
