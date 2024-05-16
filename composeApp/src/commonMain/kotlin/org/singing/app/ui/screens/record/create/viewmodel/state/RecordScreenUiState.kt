package org.singing.app.ui.screens.record.create.viewmodel.state

import com.singing.audio.player.PlayerState
import org.singing.app.domain.model.RecordPoint
import org.singing.app.ui.screens.record.create.viewmodel.model.RecordState

data class RecordScreenUiState(
    val recordState: RecordState = RecordState.STOP,
    val recordCountdown: Int? = null,

    val playerPosition: Long = 0,
    val playerState: PlayerState = PlayerState.STOP,

    val audioProcessState: AudioProcessState? = null,

    val recordStartedAt: Long = -1,
    val history: List<RecordPoint> = listOf(),
) {
    val isRecording: Boolean
        get() = recordState == RecordState.RECORD

    val isPlaying: Boolean
        get() = playerState == PlayerState.PLAY

    val canEditPlayerState: Boolean
        get() = recordState == RecordState.STOP
}
