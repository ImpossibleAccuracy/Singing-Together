package com.singing.feature.recording.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.singing.app.common.views.base.AppTextButton
import com.singing.app.common.views.base.list.EmptyView
import com.singing.app.ui.formatFrequency
import com.singing.app.ui.screen.dimens
import com.singing.app.ui.screen.icon
import com.singing.feature.recording.domain.model.AudioInputData
import com.singing.feature.recording.presenter.generated.resources.Res
import com.singing.feature.recording.presenter.generated.resources.action_dismiss
import com.singing.feature.recording.presenter.generated.resources.baseline_mic_24
import com.singing.feature.recording.presenter.generated.resources.baseline_volume_up_black_24dp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


@Composable
fun AudioDisplay(
    modifier: Modifier = Modifier,
    input: AudioInputData,
    recordCountdown: Int?,
    note: (Double) -> String,
    stopRecordCountdown: () -> Unit,
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .let {
                    if (recordCountdown == null) it
                    else it.blur(16.dp)
                }
        ) {
            val firstInputColor = MaterialTheme.colorScheme.primaryContainer
            val secondInputColor = MaterialTheme.colorScheme.secondaryContainer

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .let {
                        when (input.secondInput) {
                            null -> it.background(firstInputColor)

                            else -> it.background(
                                brush = Brush.linearGradient(
                                    0.4f to firstInputColor,
                                    0.6f to secondInputColor,
                                )
                            )
                        }
                    }
            ) {
                if (input.isEnabled) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        if (input.firstInput == null) {
                            EmptyView(
                                title = "Sing something...",
                                subtitle = "Note you sang will be displayed here",
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        } else {
                            InputLineDisplay(
                                modifier = Modifier.fillMaxSize(),
                                note = note(input.firstInput!!),
                                frequency = formatFrequency(input.firstInput!!),
                                icon = vectorResource(Res.drawable.baseline_mic_24),
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        }
                    }

                    if (input.secondInput != null) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            InputLineDisplay(
                                modifier = Modifier.fillMaxSize(),
                                note = note(input.secondInput!!),
                                frequency = formatFrequency(input.secondInput!!),
                                icon = vectorResource(Res.drawable.baseline_volume_up_black_24dp),
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            )
                        }
                    }
                } else {
                    EmptyView(
                        modifier = Modifier.fillMaxSize(),
                        title = "No inputs available",
                        subtitle = "Plug in any microphone to continue",
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        }

        if (recordCountdown != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme
                            .inverseSurface
                            .copy(alpha = 0.4F)
                    ),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1_5, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier,
                    text = recordCountdown.toString(),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.displayMedium,
                )

                AppTextButton(
                    modifier = Modifier.widthIn(min = 146.dp),
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    label = stringResource(Res.string.action_dismiss),
                    onClick = stopRecordCountdown,
                )
            }
        }
    }
}


@Composable
fun InputLineDisplay(
    modifier: Modifier = Modifier,
    note: String,
    frequency: String,
    icon: ImageVector,
    contentColor: Color,
) {
    Column(
        modifier = modifier
            .padding(
                horizontal = MaterialTheme.dimens.dimen3,
                vertical = MaterialTheme.dimens.dimen2,
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier.size(size = MaterialTheme.dimens.icon),
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
        )

        Spacer(Modifier.height(MaterialTheme.dimens.dimen1))

        Text(
            text = note,
            color = contentColor,
            style = TextStyle(
                fontSize = 45.sp,
                fontWeight = FontWeight.Black
            )
        )

        Text(
            text = frequency,
            color = contentColor,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Light
            )
        )
    }
}
