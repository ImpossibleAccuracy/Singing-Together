package com.singing.app.common.views.shared.record

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.singing.app.ui.formatFrequency
import com.singing.app.ui.screen.dimens

@Composable
fun RecordTimelineItem(
    first: Double?,
    second: Double?,
    note: (Double) -> String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(
            MaterialTheme.dimens.dimen1_5,
            Alignment.CenterVertically
        ),
    ) {
        val firstNote = first?.let { note(it) } ?: "---"
        val secondNote = second?.let { note(it) }
        val noteText =
            if (secondNote == null) firstNote
            else "$firstNote ($secondNote)"

        val frequencyText =
            if (first == null) null
            else if (second == null) formatFrequency(first)
            else formatFrequency(first - second)

        Text(
            text = noteText,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
        )

        if (frequencyText != null) {
            Text(
                text = frequencyText,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}
