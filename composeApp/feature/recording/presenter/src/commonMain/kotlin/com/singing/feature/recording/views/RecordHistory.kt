package com.singing.feature.recording.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import com.singing.app.ui.formatFrequency
import com.singing.app.ui.formatTimeString
import com.singing.app.ui.screen.dimens
import com.singing.domain.model.RecordPoint
import com.singing.feature.recording.presenter.generated.resources.*
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

private const val NoInputDataText = "---"


@Composable
fun RecordHistory(
    modifier: Modifier = Modifier,
    history: ImmutableList<RecordPoint>,
    note: (Double) -> String,
) {
    var isHistoryVisible by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(
                horizontal = MaterialTheme.dimens.dimen3,
                vertical = MaterialTheme.dimens.dimen2,
            ),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(Res.string.label_recording),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
            )

            Spacer(Modifier.weight(1f))

            IconButton(
                onClick = {
                    isHistoryVisible = !isHistoryVisible
                }
            ) {
                if (isHistoryVisible) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.baseline_visibility_24),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = "",
                    )
                } else {
                    Icon(
                        imageVector = vectorResource(Res.drawable.baseline_visibility_off_24),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = "",
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = isHistoryVisible
        ) {
            if (history.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.dimens.dimen3)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(Res.string.label_no_history),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                    )
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1),
                ) {
                    repeat(history.size) {
                        val index = history.size - 1 - it
                        val item = history[index]

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(shape = MaterialTheme.shapes.small)
                                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                                .padding(
                                    horizontal = MaterialTheme.dimens.dimen1_5,
                                    vertical = MaterialTheme.dimens.dimen1,
                                ),
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1),
                        ) {
                            Text(
                                text = formatTimeString(item.time),
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = MaterialTheme.typography.labelMedium,
                            )

                            Spacer(Modifier.width(MaterialTheme.dimens.dimen0_5))

                            Text(
                                text = if (item.first == null) NoInputDataText else note(item.first!!),
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = MaterialTheme.typography.bodyLarge,
                            )

                            if (item.second != null) {
                                Text(
                                    text = "(${note(item.second!!)})",
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    style = MaterialTheme.typography.titleMedium,
                                )
                            }

                            Spacer(Modifier.weight(1f))

                            Text(
                                text = if (item.first == null) NoInputDataText else formatFrequency(item.first!!),
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = MaterialTheme.typography.labelLarge,
                            )

                            if (item.second != null) {
                                Text(
                                    text = "(${formatFrequency(item.second!!)})",
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    style = MaterialTheme.typography.labelMedium,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
