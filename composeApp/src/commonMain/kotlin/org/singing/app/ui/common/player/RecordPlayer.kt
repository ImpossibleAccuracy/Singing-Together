package org.singing.app.ui.common.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.screen.Screen
import com.singing.audio.player.AudioPlayer
import com.singing.audio.player.PlayerState
import com.singing.audio.player.multiplyPlayers
import com.singing.audio.utils.ComposeFile
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.singing.app.di.module.viewModels
import org.singing.app.domain.model.RecordData
import org.singing.app.domain.repository.record.RecordRepository


class RecordPlayer(
    private val recordRepository: RecordRepository,
) {
    companion object {
        private const val POSITION_UPDATE_RATE = 300L
    }

    private val recorderScope = CoroutineScope(Dispatchers.Default)

    private val _state = MutableStateFlow(PlayerState.STOP)
    val state = _state.asStateFlow()

    private val _position = MutableStateFlow<Long>(0)
    val position = _position.asStateFlow()

    private var positionLoopJob: Job? = null
    private val player1 = AudioPlayer()
    private val player2 = AudioPlayer()


    suspend fun play(recordData: RecordData) {
        val voiceFile = recordRepository.getRecordVoiceFile(recordData)

        // TODO: extract magic number
        _position.value =
            if (position.value >= recordData.duration * 0.995) 0
            else position.value

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
                _state.value = playerState
            }
    }

    private suspend fun playWithTrack(voiceFile: ComposeFile, trackFile: ComposeFile) = coroutineScope {
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

                _state.value = playerState
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

    suspend fun reset() {
        positionLoopJob?.cancel()

        stop()
        setPosition(0)
    }

    suspend fun setPosition(newPosition: Long) {
        player1.setPosition(newPosition)
        player2.setPosition(newPosition)

        _position.value = newPosition
    }

    suspend fun stop() {
        _state.value = PlayerState.STOP

        player1.stop()
        player2.stop()
    }
}

@Composable
fun Screen.rememberRecordPlayer(): RecordPlayer {
    val viewModel = viewModels<RecordPlayerViewModel>()

    LifecycleEffect {
        viewModel.stopRecordPlayer()
    }

    return remember { viewModel.recordPlayer }
}
