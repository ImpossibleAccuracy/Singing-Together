package org.singing.app.ui.screens.record.views.audio

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.*
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.baseline_close_black_24dp
import com.singing.app.composeapp.generated.resources.baseline_folder_open_black_24dp
import com.singing.app.composeapp.generated.resources.baseline_play_arrow_black_24dp
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.ui.helper.Space
import org.singing.app.ui.helper.getTimeString

data class AudioTrackControlsData(
    val duration: Long,
    val currentPosition: Long? = null,
    val isPlaying: Boolean = true,
    val isEditable: Boolean = true,
) {
    internal val playerPosition: Double
        get() = currentPosition!! / duration.toDouble()
}

@Immutable
data class AudioTrackControlsActions(
    val onTrackPositionChange: ((position: Float) -> Unit)? = null,
    val onChangeTrack: () -> Unit,
    val onRemoveTrack: () -> Unit,
    val onPreviewClick: () -> Unit,
)


@Composable
fun AudioTrackControls(
    modifier: Modifier = Modifier,
    data: AudioTrackControlsData,
    actions: AudioTrackControlsActions,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            AssistChip(
                enabled = data.isEditable,
                onClick = {
                    actions.onChangeTrack()
                },
                leadingIcon = {
                    Icon(
                        imageVector = vectorResource(Res.drawable.baseline_folder_open_black_24dp),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                    )
                },
                label = {
                    Text(
                        text = "Заменить",
                        color = MaterialTheme.colorScheme.tertiary,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            )

            Space(12.dp)

            AssistChip(
                enabled = data.isEditable,
                onClick = {
                    actions.onRemoveTrack()
                },
                leadingIcon = {
                    Icon(
                        imageVector = vectorResource(Res.drawable.baseline_close_black_24dp),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                    )
                },
                label = {
                    Text(
                        text = "Убрать",
                        color = MaterialTheme.colorScheme.tertiary,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            )
        }

        Space(12.dp)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
        ) {
            Text(
                text = data.currentPosition?.let { getTimeString(it) } ?: "00:00",
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.labelMedium
            )

            Space(4.dp)

            Slider(
                modifier = Modifier.weight(1f),
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.tertiary,
                    activeTrackColor = MaterialTheme.colorScheme.tertiary,
                    inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerLow,
                ),
                value = if (data.currentPosition == null) {
                    0F
                } else {
                    data.playerPosition.toFloat()
                },
                onValueChange = {
                    if (data.isEditable) {
                        actions.onTrackPositionChange?.invoke(it)
                    }
                }
            )

            Space(4.dp)

            Text(
                text = getTimeString(data.duration),
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.labelMedium
            )
        }

        Space(8.dp)

        FilledTonalButton(
            enabled = data.isEditable,
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
            ),
            onClick = {
                actions.onPreviewClick()
            }
        ) {
            val text = if (data.isPlaying) "Остановить" else "Прослушать"
            val icon =
                if (data.isPlaying) Res.drawable.baseline_stop_black_24dp else Res.drawable.baseline_play_arrow_black_24dp

            Text(
                text = text,
                color = MaterialTheme.colorScheme.onTertiary,
                style = MaterialTheme.typography.labelLarge,
            )

            Space(8.dp)

            Icon(
                modifier = Modifier.size(18.dp),
                imageVector = vectorResource(icon),
                contentDescription = "play",
                tint = MaterialTheme.colorScheme.onTertiary,
            )
        }
    }
}
