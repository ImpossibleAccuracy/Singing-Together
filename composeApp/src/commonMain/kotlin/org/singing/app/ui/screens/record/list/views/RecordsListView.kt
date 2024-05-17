package org.singing.app.ui.screens.record.list.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.singing.app.domain.model.RecordData
import org.singing.app.ui.views.base.record.card.RecordCard


@Composable
fun RecordsListView(
    modifier: Modifier = Modifier,
    selectedRecord: RecordData?,
    onSelectedRecordChange: (RecordData) -> Unit,
    records: List<RecordData>,
) {
    LazyColumn(
        modifier = modifier
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
