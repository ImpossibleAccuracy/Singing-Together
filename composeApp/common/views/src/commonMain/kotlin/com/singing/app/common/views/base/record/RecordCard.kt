package com.singing.app.common.views.base.record

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.singing.app.common.views.model.state.RecordUiData
import com.singing.app.common.views.shared.record.RecordThumb
import com.singing.app.common.views.views.generated.resources.Res
import com.singing.app.common.views.views.generated.resources.label_no_track_selected
import com.singing.app.ui.screen.dimens
import com.singing.app.ui.utils.cardAppearance
import org.jetbrains.compose.resources.stringResource

@Composable
fun RecordCard(
    modifier: Modifier = Modifier,
    data: RecordUiData,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    accuracyContainerColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .heightIn(min = MaterialTheme.dimens.dimen5_5 * 2)
            .cardAppearance(
                shape = MaterialTheme.shapes.medium,
                background = containerColor,
                padding = PaddingValues(horizontal = MaterialTheme.dimens.dimen2),
                onClick = onClick,
            ),
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
                    ?: stringResource(Res.string.label_no_track_selected),
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
