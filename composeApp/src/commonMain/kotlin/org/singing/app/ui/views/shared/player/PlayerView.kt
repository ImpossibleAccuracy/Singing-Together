package org.singing.app.ui.views.shared.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.ui.base.Space
import org.singing.app.ui.views.base.AppFilledButton
import org.singing.app.ui.views.base.progress.TimeProgress


@Composable
fun PlayerView(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.secondary,
    contentColor: Color = MaterialTheme.colorScheme.onSecondary,
    inactiveTrackColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    editable: Boolean = true,
    totalDuration: Long,
    currentPosition: Long,
    isPlaying: Boolean,
    onPositionChange: (Long) -> Unit,
    onPlay: () -> Unit,
    onStop: () -> Unit,
) {
    Column(
        modifier = Modifier
            .clip(shape = MaterialTheme.shapes.large)
            .background(color = containerColor)
                then modifier
    ) {
        TimeProgress(
            modifier = Modifier.fillMaxWidth(),
            contentColor = contentColor,
            inactiveTrackColor = inactiveTrackColor,
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

            val icon = vectorResource(
                when (isPlaying) {
                    true -> Res.drawable.baseline_stop_black_24dp
                    false -> Res.drawable.baseline_play_arrow_black_24dp
                }
            )

            AppFilledButton(
                modifier = Modifier.widthIn(min = 180.dp),
                enabled = editable,
                containerColor = contentColor,
                label = actionText,
                trailingIcon = icon,
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
