package org.singing.app.ui.screens.record.viewmodel

import com.singing.audio.player.AudioPlayer
import com.singing.audio.player.PlayerState
import com.singing.config.note.NotesStore
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.singing.app.domain.store.VoiceInput
import org.singing.app.ui.base.AppViewModel
import org.singing.app.ui.screens.record.viewmodel.model.RecordItem
import org.singing.app.ui.screens.record.viewmodel.model.RecordPair
import org.singing.app.ui.screens.record.viewmodel.model.RecordState
import org.singing.app.ui.screens.record.viewmodel.state.AudioProcessState
import org.singing.app.ui.screens.record.viewmodel.state.RecordData
import org.singing.app.ui.screens.record.viewmodel.state.RecordScreenUiState

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class RecordViewModel : AppViewModel() {
    private val player = AudioPlayer()

    private var countdownJob: Job? = null
    private val _uiState = MutableStateFlow(RecordScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _recordData = MutableStateFlow(RecordData())
    val recordData = _recordData.asStateFlow()

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

    private val _firstInput = MutableStateFlow<Flow<RecordItem>?>(null)
    private val _secondInput = MutableStateFlow<Flow<RecordItem>?>(null)

    private val isRecording: Boolean
        get() = uiState.value.isRecording

    private val isPlaying: Boolean
        get() = uiState.value.isPlaying

    val isAnyActionActive: Boolean
        get() = uiState.value.isPlaying || uiState.value.recordState != RecordState.STOP

    init {
        listenForInputs()
        listenForVoiceInput()
        listenForSelectedAudio()
    }

    // ---------------- INIT FUNCTIONS ----------------

    private fun listenForInputs() = viewModelScope.launch {
        launch {
            _firstInput
                .flatMapConcat { it ?: flowOf(null) }
                .collect { data ->
                    _recordData.update {
                        it.copy(
                            firstInput = data
                        )
                    }
                }
        }

        launch {
            _secondInput
                .flatMapConcat { it ?: flowOf(null) }
                .collect { data ->
                    _recordData.update {
                        it.copy(
                            secondInput = data
                        )
                    }
                }
        }

        launch {
            recordData
                .map { it.firstInput to it.secondInput }
                .distinctUntilChanged()
                .sample(1000)
                .collect { pair ->
                    if (isRecording) {
                        _uiState.update {
                            it.copy(
                                history = ArrayList<RecordPair>(it.history.size + 1).apply {
                                    add(
                                        RecordPair(
                                            first = pair.first,
                                            second = pair.second,
                                            time = getRecordOffsetTime()
                                        )
                                    )

                                    addAll(it.history)
                                }
                            )
                        }
                    }
                }
        }
    }

    private fun listenForVoiceInput() = viewModelScope.launch {
        VoiceInput.isEnabled.collect { isEnabled ->
            _recordData.update {
                it.copy(
                    isAnyInputEnabled = isEnabled,
                )
            }

            when (isEnabled) {
                true -> {
                    _firstInput.value = VoiceInput.voiceData
                        .map {
                            buildRecordItem(frequency = it)
                        }
                        .stateIn(
                            viewModelScope,
                            SharingStarted.Lazily,
                            NotesStore.default().let {
                                RecordItem(
                                    frequency = it.frequency,
                                    note = it.note,
                                    time = 0,
                                )
                            }
                        )
                }

                false -> {
                    _firstInput.value = null
                }
            }
        }
    }

    private fun listenForSelectedAudio() = viewModelScope.launch {
        val playerPositionFlow = uiState.map { it.playerPosition }.distinctUntilChanged()

        uiState
            .map { it.audioProcessState }
            .distinctUntilChanged()
            .collect { audioState ->
                _secondInput.value = when {
                    audioState?.data == null || audioState.isParsing -> null

                    // When player position changes, when will new note be emitted
                    else -> playerPositionFlow.map { playerPosition ->
                        audioState.data.firstOrNull {
                            it.time >= playerPosition
                        } ?: RecordItem("ERROR", -1.0, -1)
                    }
                }
            }
    }


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

            _uiState.value = RecordScreenUiState()
        }
    }

    override fun onDispose() {
        stopActionsAndClearData()

        super.onDispose()
    }

    // ---------------- INTERNAL FUNCTIONS ----------------

    private fun buildRecordItem(frequency: Double, time: Long = getRecordOffsetTime()) =
        RecordItem(
            note = NotesStore.findNote(frequency),
            frequency = frequency,
            time = time
        )

    private fun getRecordOffsetTime(): Long =
        if (isRecording) System.currentTimeMillis() - uiState.value.recordStartedAt
        else 0


    // ---------------- USER FUNCTIONS ----------------

    fun setPlayerPosition(newPosition: Long) {
        viewModelScope.launch {
            assertNotRecording()

            _uiState.update {
                it.copy(
                    playerPosition = newPosition
                )
            }

            if (player.isPlaying()) {
                player.setPosition(newPosition)
            }
        }
    }

    private fun startPlayerLoop() {
        viewModelScope.launch {
            player.createPositionFlow()
                .collect { newPosition ->
                    _uiState.update {
                        it.copy(
                            playerPosition = newPosition
                        )
                    }
                }
        }
    }

    fun setProcessedAudio(audioProcessState: AudioProcessState) {
        _uiState.update {
            it.copy(
                playerPosition = 0,
                audioProcessState = audioProcessState
            )
        }
    }

    fun clearSelectedAudio() {
        assertNotRecording()

        _uiState.update {
            it.copy(
                audioProcessState = null
            )
        }

        _recordData.update {
            it.copy(
                secondInput = null,
            )
        }
    }


    fun startRecord() {
        assertNotRecording()

        viewModelScope.launch {
            startRecordCountdown().let {
                it.join()

                if (it.isCancelled) {
                    return@launch
                }
            }

            prepareToRecord()

            val selectedAudio = uiState.value.audioProcessState?.selectedAudio

            if (selectedAudio != null) {
                startPlaying()
            }

            _uiState.update {
                it.copy(
                    recordState = RecordState.RECORD
                )
            }
        }
    }

    private fun prepareToRecord() {
        if (isPlaying) {
            startPlaying()
        }

        _uiState.update {
            it.copy(
                playerPosition = 0,
                recordStartedAt = System.currentTimeMillis(),
                history = listOf(),
            )
        }
    }

    private fun startRecordCountdown() =
        viewModelScope.async {
            _uiState.update {
                it.copy(
                    recordState = RecordState.COUNTDOWN
                )
            }

            for (i in 3 downTo 1) {
                _uiState.update {
                    it.copy(
                        recordCountdown = i
                    )
                }

                delay(1000)
            }

            _uiState.update {
                it.copy(
                    recordCountdown = null
                )
            }
        }
            .also {
                countdownJob = it
            }

    fun stopRecordCountdown() {
        countdownJob?.cancel()

        if (uiState.value.recordState == RecordState.COUNTDOWN) {
            _uiState.update {
                it.copy(
                    recordState = RecordState.STOP,
                    recordCountdown = null,
                )
            }
        }
    }

    fun stopRecord() {
        stopPlaying()

        _uiState.update {
            it.copy(
                recordStartedAt = -1,
                recordState = RecordState.STOP,
            )
        }
    }


    fun startPlaying() {
        viewModelScope.launch {
            val track = uiState.value.audioProcessState?.selectedAudio
                ?: throw NullPointerException("Set selected audio before call startPlaying()")

            if (player.isPlaying()) {
                player.setPosition(0)
            } else {
                val totalDuration = track.duration
                val initPosition = uiState.value.playerPosition

                val startPosition =
                    if (initPosition >= totalDuration * 0.995) 0
                    else initPosition

                player
                    .play(track, startPosition)
                    .onEach {
                        if (it == PlayerState.PLAY) {
                            startPlayerLoop()
                        }
                    }
                    .collect { playerState ->
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
                    }
            }
        }
    }

    fun stopPlaying() {
        viewModelScope.launch {
            if (player.isPlaying()) {
                player.stop()
            }
        }
    }

    private fun assertNotRecording() {
        if (isRecording) {
            throw IllegalStateException("Call was rejected because player is recording now.")
        }
    }
}
