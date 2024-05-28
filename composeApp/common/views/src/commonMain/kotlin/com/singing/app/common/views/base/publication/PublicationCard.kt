package com.singing.app.common.views.base.publication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.singing.app.common.views.model.actions.PublicationCardActions
import com.singing.app.common.views.model.state.PublicationUiData
import com.singing.app.common.views.views.generated.resources.Res
import com.singing.app.common.views.views.generated.resources.action_listen_now
import com.singing.app.common.views.views.generated.resources.action_see_record
import com.singing.app.common.views.views.generated.resources.baseline_play_circle_filled_24
import com.singing.app.ui.utils.cardAppearance
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


@Composable
@ReadOnlyComposable
fun publicationCardAppearance(
    containerColor: Color = MaterialTheme.colorScheme.surface,
    shape: Shape = MaterialTheme.shapes.medium,
    padding: PaddingValues = PaddingValues(12.dp)
) = Modifier.cardAppearance(
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    shape = shape,
    background = containerColor,
    padding = padding,
)


@Composable
fun PublicationCard(
    modifier: Modifier = publicationCardAppearance(),
    data: PublicationUiData,
    actions: PublicationCardActions,
) {
    BasePublicationCard(
        modifier = modifier,
        data = data,
        actions = actions,
        slotAfterAuthor = {},
        slotAfterDescription = {
            if (data.showActions) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AssistChip(
                        label = {
                            Text(
                                text = stringResource(Res.string.action_see_record),
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.labelLarge,
                            )
                        },
                        onClick = {
                            actions.navigatePublicationDetails()
                        }
                    )

                    Spacer(Modifier.width(8.dp))

                    AssistChip(
                        label = {
                            Text(
                                text = stringResource(Res.string.action_listen_now),
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.labelLarge,
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = vectorResource(Res.drawable.baseline_play_circle_filled_24),
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = ""
                            )
                        },
                        onClick = {
                            actions.onPlay?.invoke()
                        }
                    )
                }
            }
        }
    )
}

