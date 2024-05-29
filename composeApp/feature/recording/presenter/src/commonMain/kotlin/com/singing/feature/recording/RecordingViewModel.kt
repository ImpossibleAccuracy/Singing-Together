package com.singing.feature.recording

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.singing.app.domain.provider.UserProvider
import com.singing.app.domain.usecase.FindNoteUseCase
import com.singing.domain.model.RecordPoint
import com.singing.feature.recording.domain.ClockFlow
import com.singing.feature.recording.domain.InputListener
import com.singing.feature.recording.domain.PlayerHelper
import com.singing.feature.recording.domain.RecordHelper
import com.singing.feature.recording.domain.model.AudioInputData
import com.singing.feature.recording.domain.model.RecordState
import com.singing.feature.recording.viewmodel.RecordingIntent
import com.singing.feature.recording.viewmodel.RecordingState
import com.singing.feature.recording.viewmodel.RecordingUiState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.plus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RecordingViewModel(
    clockFlow: ClockFlow,
    private val inputListener: InputListener,
    private val playerHelper: PlayerHelper,
    private val recordHelper: RecordHelper,

    private val userProvider: UserProvider,
    private val findNoteUseCase: FindNoteUseCase,
) : ScreenModel {
    private val state = MutableStateFlow(RecordingState())
    val uiState = state
        .map { RecordingUiState.fromViewModelState(it) }
        .flowOn(Dispatchers.Default)
        .stateIn(
            screenModelScope,
            SharingStarted.WhileSubscribed(5000),
            RecordingUiState.fromViewModelState(state.value)
        )

    val recordDuration = clockFlow.time
        .combine(state) { time, state ->
            state.recordStartedAt?.let { time to it }
        }
        .flowOn(Dispatchers.Default)
        .filterNotNull()
        .distinctUntilChanged()
        .map { (currentTime, startTime) -> currentTime - startTime }
        .stateIn(
            screenModelScope,
            SharingStarted.WhileSubscribed(5000),
            -1,
        )

    val audioInput = inputListener.audioInputData
        .onEach { inputData ->
            val data = state.value

            if (data.recordStartedAt != null) {
                this.state.update {
                    it.copy(
                        history = data.history.plus(
                            RecordPoint(
                                time = System.currentTimeMillis() - data.recordStartedAt,
                                first = inputData.firstInput,
                                second = inputData.secondInput,
                            )
                        ),
                    )
                }
            }
        }
        .stateIn(
            screenModelScope,
            SharingStarted.WhileSubscribed(5000),
            AudioInputData(firstInput = 0.0)
        )

    val recordResult = recordHelper.recordResult
        .stateIn(
            screenModelScope,
            SharingStarted.WhileSubscribed(5000),
            null,
        )

    // ---------------------------------------------------------------

    init {
        screenModelScope.launch {
            launch {
                userProvider.userFlow.collect { user ->
                    state.update { it.copy(user = user) }
                }
            }

            launch {
                inputListener.init(
                    strategy = InputListener.MergeStrategy.Combine,
                    lineParams = InputListener.LineParams(
                        state.map { it.trackData },
                        state.map { it.playerPosition },
                    )
                )
            }

            launch {
                playerHelper.isPlaying.collect { isPlaying ->
                    if (!isPlaying) {
                        onPlayerStop()
                    }

                    state.update { it.copy(isPlaying = isPlaying) }
                }
            }

            launch {
                playerHelper.position.collect { value ->
                    state.update { it.copy(playerPosition = value) }
                }
            }

            launch {
                recordHelper.countdown.collect { value ->
                    state.update { it.copy(recordCountdown = value) }
                }
            }

            launch {
                recordHelper.recordState.collect { value ->
                    when (value) {
                        RecordState.RECORD -> onRecordStart()
                        RecordState.STOP -> onRecordStop()
                        RecordState.COUNTDOWN -> onRecordCountdown()
                    }

                    state.update { it.copy(recordState = value) }
                }
            }
        }
    }

    private suspend fun onPlayerStop() {
        if (state.value.recordState == RecordState.RECORD) {
            recordHelper.stopRecord()
        }
    }

    private suspend fun onRecordStart() {
        val newState = state.updateAndGet {
            it.copy(
                recordStartedAt = System.currentTimeMillis(),
                history = persistentListOf()
            )
        }

        if (newState.trackData != null) {
            screenModelScope.launch {
                playerHelper.startPlaying(
                    track = newState.trackData.selectedAudio,
                    initPosition = 0,
                )
            }
        }
    }

    private suspend fun onRecordStop() {
        playerHelper.stopPlaying()

        state.update { it.copy(recordStartedAt = null) }
    }

    private suspend fun onRecordCountdown() {
        playerHelper.stopPlaying()
    }

    // ---------------------------------------------------------------

    fun onIntent(intent: RecordingIntent) {
        screenModelScope.launch {
            when (intent) {
                is RecordingIntent.UpdateTrack -> {
                    state.update { it.copy(trackData = intent.track) }
                }

                RecordingIntent.ClearTrack -> {
                    state.update { it.copy(trackData = null) }
                }

                RecordingIntent.StartPlaying -> {
                    if (state.value.trackData != null) {
                        playerHelper.startPlaying(
                            state.value.trackData!!.selectedAudio,
                            state.value.playerPosition,
                        )
                    }
                }

                is RecordingIntent.UpdatePlayerPosition -> {
                    state.update { it.copy(playerPosition = intent.newPosition) }
                    playerHelper.setPosition(intent.newPosition)
                }

                RecordingIntent.StopPlaying -> {
                    playerHelper.stopPlaying()
                }

                RecordingIntent.StartRecording -> {
                    recordHelper.startRecord()
                }

                RecordingIntent.StopRecording -> {
                    recordHelper.stopRecord()
                }

                RecordingIntent.StopRecordCountdown -> {
                    recordHelper.stopRecordCountdown()
                }
            }
        }
    }

    fun getNote(frequency: Double): String = findNoteUseCase(frequency)
}
