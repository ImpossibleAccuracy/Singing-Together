package org.singing.app.ui.screens.record.list.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.singing.app.domain.model.RecordData
import org.singing.app.ui.views.record.RecordCard


@Composable
fun RecordsList(
    selectedRecord: RecordData?,
    onSelectedRecordChange: (RecordData) -> Unit,
    records: List<RecordData>,
) {
    LazyColumn(
        modifier = Modifier
            .width(300.dp)
            .fillMaxHeight()
            .padding(vertical = 16.dp)
    ) {
        itemsIndexed(records) { index, item ->
            val isSelected = selectedRecord == item

            val containerColor = when (isSelected) {
                true -> MaterialTheme.colorScheme.secondaryContainer
                false -> Color.Transparent
            }

            val accuracyContainerColor = when (isSelected) {
                true -> MaterialTheme.colorScheme.surface
                false -> MaterialTheme.colorScheme.surfaceContainerHigh
            }

            RecordCard(
                record = item,
                containerColor = containerColor,
                accuracyContainerColor = accuracyContainerColor,
                onClick = {
                    onSelectedRecordChange(item)
                },
            )

            if (index != records.lastIndex) {
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}
