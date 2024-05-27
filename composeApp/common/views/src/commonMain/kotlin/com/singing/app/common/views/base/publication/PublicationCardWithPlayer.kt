package com.singing.app.common.views.base.publication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.singing.app.common.views.base.progress.TimeProgress
import com.singing.app.common.views.model.actions.PublicationCardActions
import com.singing.app.common.views.model.state.PlayerController
import com.singing.app.common.views.model.state.PublicationUiData
import com.singing.app.common.views.views.generated.resources.Res
import com.singing.app.common.views.views.generated.resources.action_see_record
import com.singing.app.common.views.views.generated.resources.baseline_play_arrow_black_24dp
import com.singing.app.common.views.views.generated.resources.baseline_stop_black_24dp
import com.singing.app.ui.utils.Divider
import com.singing.app.ui.utils.Space
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


@Composable
fun PublicationCardWithPlayer(
    modifier: Modifier = publicationCardAppearance(),
    contentColor: Color = MaterialTheme.colorScheme.primary,
    inactiveTrackColor: Color = MaterialTheme.colorScheme.primaryContainer,
    playerController: PlayerController,
    data: PublicationUiData,
    actions: PublicationCardActions,
) {
    val isPlaying by playerController.isPlaying
    val playerPosition by playerController.playerPosition

    BasePublicationCard(
        modifier = modifier,
        data = data,
        actions = actions,
        slotAfterAuthor = {
            AssistChip(
                label = {
                    Text(
                        text = stringResource(Res.string.action_see_record),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelLarge,
                    )
                },
                onClick = actions.navigatePublicationDetails,
            )
        },
        slotAfterDescription = {
            Space(4.dp)

            Divider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
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
                        tint = contentColor,
                        contentDescription = "",
                    )
                }

                TimeProgress(
                    modifier = Modifier.weight(1f),
                    contentColor = contentColor,
                    inactiveTrackColor = inactiveTrackColor,
                    editable = true,
                    totalDuration = playerController.duration,
                    currentPosition = playerPosition,
                    onPositionChange = {
                        playerController.setPosition(it)
                    },
                )
            }
        },
    )
}
