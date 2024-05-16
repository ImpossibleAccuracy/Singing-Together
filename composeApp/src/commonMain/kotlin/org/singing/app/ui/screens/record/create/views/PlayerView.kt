package org.singing.app.ui.screens.record.create.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.action_start_playing
import com.singing.app.composeapp.generated.resources.action_stop_playing
import org.jetbrains.compose.resources.stringResource
import org.singing.app.ui.base.Space
import org.singing.app.ui.views.AppFilledButton
import org.singing.app.ui.views.progress.TimeProgress


@Composable
fun PlayerView(
    editable: Boolean,
    totalDuration: Long,
    currentPosition: Long,
    isPlaying: Boolean,
    onPositionChange: (Long) -> Unit,
    onPlay: () -> Unit,
    onStop: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.large)
            .background(color = MaterialTheme.colorScheme.secondary)
            .padding(
                start = 16.dp,
                top = 18.dp,
                end = 16.dp,
                bottom = 12.dp,
            )
    ) {
        TimeProgress(
            modifier = Modifier.fillMaxWidth(),
            editable = editable,
            totalDuration = totalDuration,
            currentPosition = currentPosition,
            onPositionChange = onPositionChange,
        )

        Space(12.dp)

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            val actionText = when (isPlaying) {
                true -> stringResource(Res.string.action_stop_playing)
                false -> stringResource(Res.string.action_start_playing)
            }

            AppFilledButton(
                modifier = Modifier.widthIn(min = 180.dp),
                enabled = editable,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                label = actionText,
                onClick = {
                    if (isPlaying) {
                        onStop()
                    } else {
                        onPlay()
                    }
                }
            )
        }
    }
}
