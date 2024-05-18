package org.singing.app.ui.views.base.record.card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import nl.jacobras.humanreadable.HumanReadable
import org.singing.app.domain.model.RecordData
import org.singing.app.ui.base.Space
import org.singing.app.ui.base.formatTimeString

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
                filename = "No track selected",
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
                filename = record.filename,
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
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .heightIn(min = 84.dp)
            .clip(shape = MaterialTheme.shapes.medium)
            .background(color = containerColor)
            .clickable(enabled = onClick != null) {
                onClick?.invoke()
            }
            .padding(horizontal = 16.dp)
    ) {
        if (accuracy != null) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(size = 64.dp)
                    .clip(shape = MaterialTheme.shapes.medium)
                    .background(color = accuracyContainerColor)
            ) {
                Text(
                    text = "$accuracy %",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelLarge,
                )
            }

            Space(16.dp)
        }

        Column(
            modifier = Modifier.weight(weight = 1f)
        ) {
            Text(
                text = filename ?: "No track selected",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
            )

            Text(
                text = createdAt,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        Space(16.dp)

        Text(
            text = duration,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
