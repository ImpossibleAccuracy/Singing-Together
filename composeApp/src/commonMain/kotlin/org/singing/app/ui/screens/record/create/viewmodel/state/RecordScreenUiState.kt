package org.singing.app.ui.screens.record.create.viewmodel.state

import com.singing.audio.player.PlayerState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.singing.app.domain.model.RecordPoint
import org.singing.app.ui.screens.record.create.save.RecordSaveAdditionalInfo
import org.singing.app.ui.screens.record.create.viewmodel.model.RecordState

data class RecordScreenUiState(
    val recordState: RecordState = RecordState.STOP,
    val recordCountdown: Int? = null,

    val playerPosition: Long = 0,
    val playerState: PlayerState = PlayerState.STOP,

    val audioProcessState: AudioProcessState? = null,

    val recordStartedAt: Long = -1,
    val history: ImmutableList<RecordPoint> = persistentListOf(),
    val recordSaveData: RecordSaveAdditionalInfo? = null,
) {
    val isRecording: Boolean
        get() = recordState == RecordState.RECORD

    val isPlaying: Boolean
        get() = playerState == PlayerState.PLAY

    val canEditPlayerState: Boolean
        get() = recordState == RecordState.STOP
}
