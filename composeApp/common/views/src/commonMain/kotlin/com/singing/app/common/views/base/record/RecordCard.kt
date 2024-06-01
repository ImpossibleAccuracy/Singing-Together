package com.singing.app.common.views.base.record

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.singing.app.common.views.model.state.RecordUiData
import com.singing.app.common.views.shared.record.RecordThumb
import com.singing.app.common.views.views.generated.resources.Res
import com.singing.app.common.views.views.generated.resources.label_no_selected_track_item
import com.singing.app.ui.screen.dimens
import com.singing.app.ui.utils.cardAppearance
import org.jetbrains.compose.resources.stringResource

@Composable
fun Modifier.recordCardAppearance(
    showBorder: Boolean,
    containerColor: Color,
    onClick: (() -> Unit)?
) = cardAppearance(
    border = if (showBorder) BorderStroke(
        MaterialTheme.dimens.bordersThickness,
        MaterialTheme.colorScheme.outlineVariant
    ) else null,
    shape = MaterialTheme.shapes.medium,
    background = containerColor,
    padding = PaddingValues(
        horizontal = MaterialTheme.dimens.dimen2,
        vertical = MaterialTheme.dimens.dimen1_5,
    ),
    onClick = onClick,
)

@Composable
fun RecordCard(
    modifier: Modifier = Modifier.recordCardAppearance(
        false,
        MaterialTheme.colorScheme.surfaceContainer,
        null
    ),
    accuracyContainerColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    data: RecordUiData,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen2),
    ) {
        if (data.accuracy != null) {
            RecordThumb(
                color = accuracyContainerColor,
                size = MaterialTheme.dimens.dimen4 * 2,
                textStyle = MaterialTheme.typography.labelLarge,
                accuracy = data.accuracy,
            )
        }

        Column(
            modifier = Modifier.weight(weight = 1f)
        ) {
            Text(
                text = data.filename
                    ?: stringResource(Res.string.label_no_selected_track_item),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
            )

            Text(
                text = data.createdAt,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
            )
        }

        Text(
            text = data.duration,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
