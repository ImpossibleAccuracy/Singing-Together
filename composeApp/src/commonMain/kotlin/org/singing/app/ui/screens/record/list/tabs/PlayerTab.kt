package org.singing.app.ui.screens.record.list.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.baseline_play_arrow_black_24dp
import com.singing.app.composeapp.generated.resources.baseline_stop_black_24dp
import com.singing.audio.player.PlayerState
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.domain.model.RecordData
import org.singing.app.domain.repository.record.RecordPlayer
import org.singing.app.setup.collectAsStateSafe
import org.singing.app.ui.views.AppFilledButton
import org.singing.app.ui.views.progress.TimeProgress

internal data class PlayerTabData(
    val player: RecordPlayer,
    val record: RecordData,
)

@Composable
internal fun PlayerTab(data: PlayerTabData) {
    val coroutineScope = rememberCoroutineScope()

    val playerState by data.player.state.collectAsStateSafe()
    val playerPosition by data.player.position.collectAsStateSafe()

    TimeProgress(
        editable = true,
        contentColor = MaterialTheme.colorScheme.tertiary,
        totalDuration = data.record.duration,
        currentPosition = playerPosition,
        onPositionChange = {
            coroutineScope.launch {
                data.player.setPosition(it)
            }
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
                coroutineScope.launch {
                    if (playerState == PlayerState.STOP) {
                        data.player.play(data.record)
                    } else {
                        data.player.stop()
                    }
                }
            },
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        )
    }
}
