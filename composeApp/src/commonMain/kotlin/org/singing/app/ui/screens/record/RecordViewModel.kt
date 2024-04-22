package org.singing.app.ui.screens.record

import cafe.adriel.voyager.core.model.ScreenModel
import com.singing.audio.library.AudioParser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.singing.app.domain.model.AudioFile
import org.singing.app.domain.model.AudioFilter
import org.singing.app.domain.model.PlayerState
import org.singing.app.setup.audio.VoiceInput

class RecordViewModel : ScreenModel {
    val selectedAudio = MutableStateFlow<AudioFile?>(null)

    private val _playerPosition = MutableStateFlow<Long>(0)
    val playerPosition = _playerPosition.asStateFlow()

    private val _playerState = MutableStateFlow(PlayerState.STOP)
    val playerState = _playerState.asStateFlow()

    fun updatePlayerPosition(newPosition: Long) {
        _playerPosition.value = newPosition
    }

    fun startPlaying(preview: Boolean = false) {
        if (preview) {
            _playerState.value = PlayerState.PLAY_PREVIEW
        } else {
            _playerState.value = PlayerState.PLAY
        }
    }

    fun stopPlaying() {
        _playerState.value = PlayerState.STOP
    }

    fun togglePlaying() {
        if (_playerState.value == PlayerState.PLAY) {
            stopPlaying()
        } else {
            startPlaying()
        }
    }
}
