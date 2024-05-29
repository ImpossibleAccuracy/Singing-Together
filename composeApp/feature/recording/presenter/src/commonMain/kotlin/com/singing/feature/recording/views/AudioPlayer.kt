package com.singing.feature.recording.views

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.singing.app.common.views.shared.player.BasePlayerView
import com.singing.feature.recording.viewmodel.RecordingIntent
import com.singing.feature.recording.viewmodel.RecordingUiState

@Composable
fun AudioPlayerContainer(
    modifier: Modifier = Modifier,
    uiState: RecordingUiState,
    newIntent: (RecordingIntent) -> Unit
) {
    if (uiState.trackData != null) {
        BasePlayerView(
            modifier = modifier,
            shape = MaterialTheme.shapes.large,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.secondary,
            inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            editable = uiState is RecordingUiState.Stopped,
            isPlaying = uiState.isPlaying,
            position = uiState.playerPosition,
            duration = uiState.trackData!!.selectedAudio.duration,
            setPosition = {
                newIntent(RecordingIntent.UpdatePlayerPosition(it))
            },
            play = {
                newIntent(RecordingIntent.StartPlaying)
            },
            stop = {
                newIntent(RecordingIntent.StopPlaying)
            },
        )
    }
}
