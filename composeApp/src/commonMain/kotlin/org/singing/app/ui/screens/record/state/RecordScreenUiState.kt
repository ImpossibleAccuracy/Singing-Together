package org.singing.app.ui.screens.record.state

import com.singing.audio.library.filter.AudioFilter
import com.singing.audio.player.PlayerState
import org.singing.app.ui.screens.record.model.RecordState

data class RecordScreenUiState(
    val recordState: RecordState = RecordState.STOP,
    val recordCountdown: Int? = null,

    val playerPosition: Long = 0,
    val playerState: PlayerState = PlayerState.STOP,

    val filters: List<AudioFilter> = listOf(),

    val audioProcessState: AudioProcessState? = null,
) {
    val canEditPlayerState: Boolean
        get() = recordState == RecordState.STOP

    val canEditTrack: Boolean
        get() = playerState != PlayerState.PLAY && recordState != RecordState.COUNTDOWN
}
