package org.singing.app.ui.screens.record.views.record

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.singing.app.ui.helper.Space
import org.singing.app.ui.helper.getTimeString
import org.singing.app.ui.screens.record.model.RecordPair
import kotlin.math.roundToInt


@Composable
fun RecordHistory(
    modifier: Modifier = Modifier,
    items: List<RecordPair>,
) {
    Column(modifier = modifier) {
        Text(
            text = "Запись",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
        )

        Space(8.dp)

        if (items.isEmpty()) {
            Text(
                text = "Данные появятся после начала записи",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
        } else {
            Column {
                items.forEachIndexed { index, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = MaterialTheme.shapes.small)
                            .background(color = MaterialTheme.colorScheme.surfaceContainer)
                            .padding(
                                horizontal = 12.dp,
                                vertical = 8.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = getTimeString(item.first!!.time),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )

                        Space(12.dp)

                        Text(
                            text = item.first!!.note,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )

                        Spacer(Modifier.weight(1f))

                        Text(
                            text = "${item.first!!.frequency.roundToInt()} Hz",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }

                    if (index != items.lastIndex) {
                        Space(8.dp)
                    }
                }
            }
        }
    }
}
