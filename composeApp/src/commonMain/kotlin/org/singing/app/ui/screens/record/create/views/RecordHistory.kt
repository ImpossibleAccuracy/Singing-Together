package org.singing.app.ui.screens.record.create.views

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
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.*
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import com.singing.app.domain.model.RecordPoint
import org.singing.app.ui.base.Space
import org.singing.app.ui.base.formatFrequency
import org.singing.app.ui.base.formatTimeString


@Composable
fun RecordHistory(
    history: ImmutableList<RecordPoint>,
    note: (Double) -> String,
) {
    var isHistoryVisible by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            )
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

        Space(8.dp)

        AnimatedVisibility(
            visible = isHistoryVisible
        ) {
            if (history.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
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
                    verticalArrangement = Arrangement.spacedBy(8.dp),
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
                                    horizontal = 12.dp,
                                    vertical = 8.dp
                                )
                        ) {
                            Text(
                                text = formatTimeString(item.time),
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = MaterialTheme.typography.labelMedium,
                            )

                            Space(12.dp)

                            Text(
                                text = note(item.first),
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = MaterialTheme.typography.bodyLarge,
                            )

                            if (item.second != null) {
                                Space(4.dp)

                                Text(
                                    text = "(${note(item.second!!)})",
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    style = MaterialTheme.typography.titleMedium,
                                )
                            }

                            Spacer(Modifier.weight(1f))

                            Text(
                                text = formatFrequency(item.first),
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = MaterialTheme.typography.labelLarge,
                            )

                            if (item.second != null) {
                                Space(4.dp)

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
