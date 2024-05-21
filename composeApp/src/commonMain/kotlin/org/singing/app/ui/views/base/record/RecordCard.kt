package org.singing.app.ui.views.base.record

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.label_no_track_selected
import nl.jacobras.humanreadable.HumanReadable
import org.jetbrains.compose.resources.stringResource
import org.singing.app.domain.model.RecordData
import org.singing.app.ui.base.cardAppearance
import org.singing.app.ui.base.formatTimeString
import org.singing.app.ui.views.shared.record.RecordThumb

@Composable
fun RecordCard(
    modifier: Modifier = Modifier,
    record: RecordData,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    accuracyContainerColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    onClick: (() -> Unit)? = null,
) {
    when (record) {
        is RecordData.Vocal -> {
            RecordCard(
                modifier = modifier,
                accuracy = null,
                filename = stringResource(Res.string.label_no_track_selected),
                createdAt = HumanReadable.timeAgo(record.createdAt.instant),
                duration = formatTimeString(record.duration),
                containerColor = containerColor,
                accuracyContainerColor = accuracyContainerColor,
                onClick = onClick,
            )
        }

        is RecordData.Cover -> {
            RecordCard(
                modifier = modifier,
                accuracy = record.accuracy,
                filename = record.name,
                createdAt = HumanReadable.timeAgo(record.createdAt.instant),
                duration = formatTimeString(record.duration),
                containerColor = containerColor,
                accuracyContainerColor = accuracyContainerColor,
                onClick = onClick,
            )
        }
    }
}

@Composable
fun RecordCard(
    modifier: Modifier = Modifier,
    accuracy: Int?,
    filename: String?,
    createdAt: String,
    duration: String,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    accuracyContainerColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .heightIn(min = 84.dp)
            .cardAppearance(
                shape = MaterialTheme.shapes.medium,
                background = containerColor,
                padding = PaddingValues(horizontal = 16.dp),
                onClick = onClick,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (accuracy != null) {
            RecordThumb(
                color = accuracyContainerColor,
                size = 64.dp,
                textStyle = MaterialTheme.typography.labelLarge,
                accuracy = accuracy,
            )
        }

        Column(
            modifier = Modifier.weight(weight = 1f)
        ) {
            Text(
                text = filename ?: stringResource(Res.string.label_no_track_selected),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
            )

            Text(
                text = createdAt,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
            )
        }

        Text(
            text = duration,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
