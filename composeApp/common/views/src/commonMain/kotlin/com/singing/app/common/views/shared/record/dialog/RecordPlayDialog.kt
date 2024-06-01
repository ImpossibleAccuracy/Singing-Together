package com.singing.app.common.views.shared.record.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.singing.app.common.views.base.AppTextButton
import com.singing.app.common.views.base.IconLabel
import com.singing.app.common.views.base.account.UserAvatar
import com.singing.app.common.views.base.progress.TimeProgress
import com.singing.app.common.views.model.state.PlayerController
import com.singing.app.common.views.model.state.RecordUiData
import com.singing.app.common.views.model.state.UserUiData
import com.singing.app.common.views.views.generated.resources.*
import com.singing.app.ui.screen.dimens
import com.singing.app.ui.screen.icon
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun RecordPlayDialog(
    playerController: PlayerController,
    record: RecordUiData,
    creator: UserUiData?,
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
            val isPlaying by playerController.isPlaying
            val playerPosition by playerController.playerPosition

            Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1_5)) {
                RecordPlayInfo(record, creator)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen0_5),
                ) {
                    IconButton(
                        onClick = {
                            if (isPlaying) {
                                playerController.stop()
                            } else {
                                playerController.play()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = vectorResource(
                                when (isPlaying) {
                                    true -> Res.drawable.baseline_stop_black_24dp
                                    false -> Res.drawable.baseline_play_arrow_black_24dp
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
                        totalDuration = playerController.duration,
                        currentPosition = playerPosition,
                        onPositionChange = playerController.setPosition,
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
    record: RecordUiData,
    creator: UserUiData?,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen0_5)
    ) {
        if (record.accuracy != null) {
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
            label = record.filename ?: stringResource(Res.string.label_no_selected_track_item)
        )

        if (creator != null) {
            IconLabel(
                leading = {
                    UserAvatar(avatar = creator.avatar, size = MaterialTheme.dimens.icon)
                },
                label = creator.username,
            )
        }
    }
}
