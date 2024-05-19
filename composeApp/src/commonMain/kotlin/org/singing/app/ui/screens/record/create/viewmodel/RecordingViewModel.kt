package org.singing.app.ui.screens.record.create.viewmodel

import androidx.compose.runtime.Stable
import com.singing.audio.player.PlayerState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.singing.app.domain.repository.record.RecordRepository
import org.singing.app.domain.usecase.FindNoteUseCase
import org.singing.app.ui.base.AppViewModel
import org.singing.app.ui.screens.record.create.viewmodel.model.RecordState
import org.singing.app.ui.screens.record.create.viewmodel.state.AudioProcessState
import org.singing.app.ui.screens.record.create.viewmodel.state.RecordScreenUiState
import org.singing.app.ui.screens.record.create.viewmodel.usecase.InputListener
import org.singing.app.ui.screens.record.create.viewmodel.usecase.PlayerHelper
import org.singing.app.ui.screens.record.create.viewmodel.usecase.RecordHelper

@Stable
class RecordingViewModel(
    private val findNoteUseCase: FindNoteUseCase,
    private val recordRepository: RecordRepository,
) : AppViewModel() {
    private val _uiState = MutableStateFlow(RecordScreenUiState())
    val uiState = _uiState.asStateFlow()

    val recordDuration = uiState
        .map { it.recordStartedAt to it.isRecording }
        .filter {
            it.first > 0 && it.second
        }
        .map { it.first }
        .transform {
            while (uiState.value.isRecording) {
                emit(System.currentTimeMillis() - it)

                delay(100)
            }
        }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.Lazily, -1)


    private val playerHelper = PlayerHelper(
        coroutineScope = viewModelScope,
        onPlayerStateUpdate = { playerState ->
            _uiState.update {
                it.copy(
                    playerState = playerState
                )
            }

            if (playerState == PlayerState.STOP) {
                if (isRecording) {
                    stopRecord()
                }
            }
        },
        onPlayerPositionUpdate = { newPosition ->
            _uiState.update {
                it.copy(
                    playerPosition = newPosition
                )
            }
        }
    )

    private val recordHelper = RecordHelper(
        coroutineScope = viewModelScope,
        onRecordStateUpdate = { newState ->
            _uiState.update {
                it.copy(
                    recordState = newState,
                )
            }
        },
        onNewCountdownValue = { value ->
            _uiState.update {
                it.copy(
                    recordCountdown = value
                )
            }
        },
        onBeforeRecord = {
            _uiState.update {
                it.copy(
                    playerPosition = 0,
                    recordStartedAt = System.currentTimeMillis(),
                    history = persistentListOf(),
                )
            }

            startPlaying()
        },
        onRecordDone = { result ->
            stopPlaying()

            onRecordReady(result)
        },
    )

    private val inputListener = InputListener(
        coroutineScope = viewModelScope,
        recordOffsetTime = {
            if (isRecording) System.currentTimeMillis() - uiState.value.recordStartedAt
            else null
        },
        selectedAudioFlow = uiState.map { it.audioProcessState },
        playerPositionFlow = uiState.map { it.playerPosition },
        onNewHistoryItem = {},
    )

    val audioInputData = inputListener.audioInputData


    private val isRecording: Boolean
        get() = uiState.value.isRecording

    private val isPlaying: Boolean
        get() = uiState.value.isPlaying

    val isAnyActionActive: Boolean
        get() = uiState.value.isPlaying || uiState.value.recordState != RecordState.STOP


    // ---------------- DATA CLEAR FUNCTIONS ----------------

    fun stopActionsAndClearData() {
        runBlocking {
            stopRecordCountdown()

            if (isRecording) {
                stopRecord()
            }

            if (isPlaying) {
                stopPlaying()
            }

            clearSelectedAudio()
            _uiState.value = RecordScreenUiState()
        }
    }

    override fun onDispose() {
        stopActionsAndClearData()

        super.onDispose()
    }

    // ---------------- MAIN FUNCTIONS ----------------

    private fun onRecordReady(result: ByteArray) {
        viewModelScope.launch {
            /*recordRepository.saveRecord(
                history = uiState.value.history,
                voiceRecord = result,
                audioTrack = uiState.value.audioProcessState?.selectedAudio
            )*/
        }
    }


    // ---------------- AUDIO FUNCTIONS ----------------

    fun getNote(frequency: Double): String =
        findNoteUseCase(frequency)


    fun startRecord() {
        assertNotRecording()

        recordHelper.startRecord()
    }

    fun stopRecordCountdown() {
        if (uiState.value.recordState == RecordState.COUNTDOWN) {
            recordHelper.stopRecordCountdown()
        }
    }

    fun stopRecord() {
        recordHelper.stopRecord()
    }


    fun setPlayerPosition(newPosition: Long) {
        assertNotRecording()

        playerHelper.setPosition(newPosition)
    }

    fun setProcessedAudio(audioProcessState: AudioProcessState) {
        _uiState.update {
            it.copy(
                playerPosition = 0,
                audioProcessState = audioProcessState
            )
        }
    }

    private fun clearSelectedAudio() {
        assertNotRecording()

        _uiState.update {
            it.copy(
                audioProcessState = null
            )
        }
    }

    fun startPlaying() {
        val track = uiState.value.audioProcessState?.selectedAudio
            ?: throw NullPointerException("Set setProcessedAudio() before call startPlaying()")

        val initPosition = uiState.value.playerPosition

        playerHelper.startPlaying(track, initPosition)
    }

    fun stopPlaying() {
        playerHelper.stopPlaying()
    }


    private fun assertNotRecording() {
        if (isRecording) {
            throw IllegalStateException("Call was rejected because player is recording now.")
        }
    }
}
