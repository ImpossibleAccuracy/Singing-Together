package org.singing.app.ui.views.shared.record.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.*
import com.singing.audio.player.PlayerState
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.domain.model.AccountUiData
import org.singing.app.domain.model.RecordData
import org.singing.app.setup.collectAsStateSafe
import org.singing.app.ui.common.player.RecordPlayer
import org.singing.app.ui.views.base.AppTextButton
import org.singing.app.ui.views.base.IconLabel
import org.singing.app.ui.views.base.progress.TimeProgress

@Composable
fun RecordPlayDialog(
    player: RecordPlayer,
    record: RecordData,
    author: AccountUiData?,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(Res.string.title_dialog_record_play)
            )
        },
        text = {
            val coroutineScope = rememberCoroutineScope()

            val playerState by player.state.collectAsStateSafe()
            val playerPosition by player.position.collectAsStateSafe()

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                RecordPlayInfo(record, author)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                if (playerState == PlayerState.STOP) {
                                    player.play(record)
                                } else {
                                    player.stop()
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = vectorResource(
                                when (playerState) {
                                    PlayerState.PLAY -> Res.drawable.baseline_stop_black_24dp
                                    PlayerState.STOP -> Res.drawable.baseline_play_arrow_black_24dp
                                }
                            ),
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "",
                        )
                    }

                    TimeProgress(
                        modifier = Modifier.weight(1f),
                        contentColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer,
                        editable = true,
                        totalDuration = record.duration,
                        currentPosition = playerPosition,
                        onPositionChange = {
                            coroutineScope.launch {
                                player.setPosition(it)
                            }
                        },
                    )
                }
            }
        },
        confirmButton = {
            AppTextButton(
                contentColor = MaterialTheme.colorScheme.primary,
                label = stringResource(Res.string.action_close),
                onClick = onDismiss
            )
        },
    )
}

@Composable
private fun RecordPlayInfo(
    record: RecordData,
    author: AccountUiData?
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (record is RecordData.Cover) {
            IconLabel(
                leadingIcon = vectorResource(Res.drawable.baseline_volume_up_black_24dp),
                label = stringResource(
                    resource = Res.string.label_accuracy,
                    record.accuracy,
                ),
            )
        }

        IconLabel(
            leadingIcon = vectorResource(Res.drawable.baseline_folder_music_black_24dp),
            label = when (record) {
                is RecordData.Cover -> record.name
                is RecordData.Vocal -> stringResource(Res.string.label_no_track_selected)
            }
        )

        if (author != null) {
            IconLabel(
                leadingIcon = vectorResource(Res.drawable.baseline_person_24),
                label = author.username,
            )
        }
    }
}
