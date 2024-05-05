package org.singing.app.ui.screens.record

import com.darkrockstudios.libraries.mpfilepicker.MPFile
import com.singing.audio.library.filter.AudioFilter
import com.singing.config.note.NotesStore
import com.singing.config.track.TrackProperties
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.singing.app.domain.repository.voice.VoiceInput
import com.singing.audio.player.AudioPlayer
import com.singing.audio.player.PlayerState
import org.singing.app.setup.audio.createTrackAudioParser
import org.singing.app.setup.audio.processAudioFile
import org.singing.app.ui.screens.record.model.RecordItem
import org.singing.app.ui.screens.record.model.RecordState
import org.singing.app.ui.screens.record.state.AudioProcessState
import org.singing.app.ui.screens.record.state.RecordData
import org.singing.app.ui.screens.record.state.RecordScreenUiState
import org.singing.app.ui.viewmodel.AppViewModel
import org.singing.app.ui.viewmodel.viewModelScope

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class RecordViewModel : AppViewModel {
    private val player = AudioPlayer()

    private val _uiState = MutableStateFlow(RecordScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _recordData = MutableStateFlow(RecordData())
    val recordData = _recordData.asStateFlow()

    private val _firstInput = MutableStateFlow<Flow<RecordItem>?>(null)
    private val _secondInput = MutableStateFlow<Flow<RecordItem>?>(null)

    private val isRecording: Boolean
        get() = uiState.value.recordState == RecordState.RECORD

    init {
        listenForInputs()
        listenForInputFilters()
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
                        _recordData.update {
                            it.copy(
                                history = it.history.plus(pair)
                            )
                        }
                    }
                }
        }
    }

    private fun listenForInputFilters() = viewModelScope.launch {
        VoiceInput.voiceFilters
            .collect { filters ->
                _uiState.update {
                    it.copy(
                        filters = filters
                    )
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


    // ---------------- INTERNAL FUNCTIONS ----------------

    private fun buildRecordItem(frequency: Double, time: Long = getRecordOffsetTime()) =
        RecordItem(
            note = NotesStore.findNote(frequency),
            frequency = frequency,
            time = time
        )

    private fun getRecordOffsetTime(): Long =
        if (isRecording) System.currentTimeMillis() - recordData.value.recordStartedAt
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

    fun setVoiceFilters(filters: List<AudioFilter>) {
        VoiceInput.setFilters(filters)
    }


    fun processAudio(inputFile: MPFile<Any>) {
        assertNotRecording()

        viewModelScope.launch {
            val audioFile = processAudioFile(inputFile)
                ?: throw IllegalArgumentException("Cannot process file: $inputFile")

            _uiState.update {
                it.copy(
                    audioProcessState = AudioProcessState(
                        selectedAudio = audioFile,
                        isParsing = true,
                    )
                )
            }

            val parser = createTrackAudioParser(audioFile, TrackProperties.defaultFilters)

            val data = parser
                .parse()
                .map {
                    buildRecordItem(it.frequency, it.positionMs)
                }
                .toList()

            _uiState.update {
                it.copy(
                    playerPosition = 0,
                    audioProcessState = it.audioProcessState!!.copy(
                        isParsing = false,
                        data = data
                    )
                )
            }
        }
    }

    fun clearSelectedAudio() {
        _uiState.update {
            it.copy(
                audioProcessState = null
            )
        }
    }


    fun startRecord() {
        assertNotRecording()

        viewModelScope.launch {
            startRecordCountdown()

            prepareToRecord()

            val selectedAudio = uiState.value.audioProcessState?.selectedAudio

            if (selectedAudio != null) {
                startPlaying()
            }

            _recordData.update {
                it.copy(
                    recordStartedAt = System.currentTimeMillis()
                )
            }

            _uiState.update {
                it.copy(
                    recordState = RecordState.RECORD
                )
            }
        }
    }

    private fun prepareToRecord() {
        _recordData.update {
            it.copy(
                recordStartedAt = System.currentTimeMillis(),
                history = listOf(),
            )
        }

        _uiState.update {
            it.copy(
                playerPosition = 0,
            )
        }
    }

    private suspend fun startRecordCountdown() {
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

    fun stopRecord() {
        stopPlaying()

        _uiState.update {
            it.copy(
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
