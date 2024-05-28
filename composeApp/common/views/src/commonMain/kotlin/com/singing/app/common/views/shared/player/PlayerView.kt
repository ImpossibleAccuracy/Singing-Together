package com.singing.app.common.views.shared.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.singing.app.common.views.base.AppFilledButton
import com.singing.app.common.views.base.progress.TimeProgress
import com.singing.app.common.views.model.state.PlayerController
import com.singing.app.common.views.views.generated.resources.*
import com.singing.app.ui.screen.dimens
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


@Composable
fun PlayerView(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    containerColor: Color = MaterialTheme.colorScheme.secondary,
    contentColor: Color = MaterialTheme.colorScheme.onSecondary,
    inactiveTrackColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    editable: Boolean = true,
    playerController: PlayerController,
) {
    val isPlaying by playerController.isPlaying
    val position by playerController.playerPosition

    Column(
        modifier = modifier
            .clip(shape)
            .background(color = containerColor)
            .padding(
                start = MaterialTheme.dimens.dimen2,
                top = MaterialTheme.dimens.dimen0_5,
                end = MaterialTheme.dimens.dimen2,
                bottom = MaterialTheme.dimens.dimen1_5,
            ),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1_5),
    ) {
        TimeProgress(
            modifier = Modifier.fillMaxWidth(),
            contentColor = contentColor,
            inactiveTrackColor = inactiveTrackColor,
            editable = editable,
            totalDuration = playerController.duration,
            currentPosition = position,
            onPositionChange = playerController.setPosition,
        )

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
                        playerController.stop()
                    } else {
                        playerController.play()
                    }
                }
            )
        }
    }
}
