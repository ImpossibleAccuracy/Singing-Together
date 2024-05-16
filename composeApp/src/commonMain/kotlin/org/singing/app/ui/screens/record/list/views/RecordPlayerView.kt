package org.singing.app.ui.screens.record.list.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.baseline_play_arrow_black_24dp
import com.singing.app.composeapp.generated.resources.baseline_stop_black_24dp
import com.singing.audio.player.PlayerState
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.ui.views.base.AppFilledButton
import org.singing.app.ui.views.base.progress.TimeProgress

@Composable
fun RecordPlayerView(
    modifier: Modifier,
    duration: Long,
    playerState: PlayerState,
    playerPosition: Long,
    updatePosition: (Long) -> Unit,
    startPlay: () -> Unit,
    stopPlay: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .padding(
                start = 24.dp,
                top = 4.dp,
                end = 24.dp,
                bottom = 16.dp,
            )
    ) {
        TimeProgress(
            editable = true,
            contentColor = MaterialTheme.colorScheme.tertiary,
            totalDuration = duration,
            currentPosition = playerPosition,
            onPositionChange = {
                updatePosition(it)
            },
        )

        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            AppFilledButton(
                modifier = Modifier.widthIn(min = 180.dp),
                label = when (playerState) {
                    PlayerState.PLAY -> "Stop"
                    PlayerState.STOP -> "Listen"
                },
                trailingIcon = vectorResource(
                    when (playerState) {
                        PlayerState.PLAY -> Res.drawable.baseline_stop_black_24dp
                        PlayerState.STOP -> Res.drawable.baseline_play_arrow_black_24dp
                    }
                ),
                onClick = {
                    if (playerState == PlayerState.STOP) {
                        startPlay()
                    } else {
                        stopPlay()
                    }
                },
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            )
        }
    }
}
