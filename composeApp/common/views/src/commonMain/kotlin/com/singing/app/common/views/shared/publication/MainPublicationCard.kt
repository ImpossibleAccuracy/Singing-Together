package com.singing.app.common.views.shared.publication

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.singing.app.common.views.base.progress.TimeProgress
import com.singing.app.common.views.base.publication.BasePublicationCard
import com.singing.app.common.views.base.publication.publicationCardAppearance
import com.singing.app.common.views.model.actions.PublicationCardActions
import com.singing.app.common.views.model.state.PlayerController
import com.singing.app.common.views.model.state.PublicationUiData
import com.singing.app.common.views.model.state.RecordUiData
import com.singing.app.common.views.views.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


val chipShape
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.shapes.medium

@Composable
fun MainPublicationCard(
    modifier: Modifier = publicationCardAppearance(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
    ),
    playerController: PlayerController,
    recordUiData: RecordUiData,
    publicationData: PublicationUiData,
    actions: PublicationCardActions,
) {
    BasePublicationCard(
        modifier = modifier,
        data = publicationData,
        actions = actions,
        slotAfterAuthor = {
            IconButton(
                onClick = actions.navigatePublicationDetails,
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.baseline_open_in_new_24),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "",
                )
            }
        },
        slotAfterDescription = {
            PublicationCardInfo(recordUiData)

            PublicationCardPlayer(playerController)
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PublicationCardInfo(data: RecordUiData) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .height(36.dp)
                .border(1.dp, color = MaterialTheme.colorScheme.outlineVariant, shape = chipShape)
                .clip(shape = chipShape)
                .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = when (data.accuracy) {
                    null -> "N/A"
                    else -> stringResource(Res.string.label_accuracy, data.accuracy)
                },
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Row(
            modifier = Modifier
                .height(36.dp)
                .border(1.dp, color = MaterialTheme.colorScheme.outlineVariant, shape = chipShape)
                .clip(shape = chipShape)
                .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.baseline_access_time_24),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = "",
            )

            Text(
                text = data.duration,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun PublicationCardPlayer(
    playerController: PlayerController,
) {
    val isPlaying by playerController.isPlaying
    val playerPosition by playerController.playerPosition

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
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
