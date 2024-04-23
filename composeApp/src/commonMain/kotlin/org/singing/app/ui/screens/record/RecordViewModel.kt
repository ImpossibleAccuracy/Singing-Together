package org.singing.app.ui.screens.record

import com.darkrockstudios.libraries.mpfilepicker.MPFile
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.singing.app.domain.audio.player.AudioPlayer
import org.singing.app.domain.audio.player.PlayerState
import org.singing.app.domain.model.audio.AudioFile
import org.singing.app.ui.screens.record.model.PlayerUiState
import org.singing.app.ui.screens.record.model.RecorderState
import org.singing.app.ui.viewmodel.AppViewModel
import org.singing.app.ui.viewmodel.viewModelScope

class RecordViewModel : AppViewModel {
    private val _selectedAudio = MutableStateFlow<AudioFile?>(null)
    val selectedAudio = _selectedAudio.asStateFlow()

    private val player = AudioPlayer()

    private val _recorderState = MutableStateFlow(RecorderState.STOP)
    val recorderState = _recorderState.asStateFlow()

    private val _playerPosition = MutableStateFlow<Long>(0)
    val playerPosition = _playerPosition.asStateFlow()

    private val _playerState = MutableStateFlow(PlayerUiState.STOP)
    val playerState = _playerState.asStateFlow()


    suspend fun setPlayerPosition(newPosition: Long) {
        _playerPosition.value = newPosition

        if (player.isPlaying()) {
            player.setPosition(newPosition)
        }
    }

    private suspend fun startPlayerLoop() = viewModelScope.launch {
        async {
            player.createPositionFlow()
                .collect {
                    _playerPosition.value = it
                }
        }
    }


    fun setSelectedAudio(inputFile: MPFile<Any>) {
        viewModelScope.launch {
            val audioFile = processAudioFile(inputFile)

            _playerPosition.value = 0
            _selectedAudio.value = audioFile
        }
    }

    fun clearSelectedAudio() {
        _selectedAudio.value = null
    }


    suspend fun startRecord() {
        _recorderState.value = when (recorderState.value) {
            RecorderState.STOP -> RecorderState.RECORD
            RecorderState.PREVIEW -> RecorderState.RECORD
            RecorderState.RECORD -> throw IllegalStateException("Record already started")
        }

        when (recorderState.value) {
            RecorderState.RECORD, RecorderState.PREVIEW -> {
                if (selectedAudio.value != null) {
                    startPlaying()
                }
            }

            RecorderState.STOP -> throw IllegalStateException()
        }
    }

    suspend fun stopRecord() {
        _recorderState.value = RecorderState.STOP
        stopPlaying()
    }


    suspend fun startPlaying() {
        val track = selectedAudio.value ?: throw NullPointerException("Set selected audio before call startPlaying()")

        if (!player.isPlaying()) {
            stopPlaying()

            val totalDuration = selectedAudio.value!!.duration
            val initPosition = playerPosition.value

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
                .collect {
                    val newState = when (it) {
                        PlayerState.PLAY -> PlayerUiState.PLAY
                        PlayerState.STOP -> PlayerUiState.STOP
                    }

                    _playerState.value = newState

                    if (newState == PlayerUiState.STOP) {
                        if (recorderState.value == RecorderState.RECORD) {
                            stopRecord()
                        }
                    }
                }
        }
    }

    suspend fun stopPlaying() {
        if (player.isPlaying()) {
            player.stop()
        }
    }
}
