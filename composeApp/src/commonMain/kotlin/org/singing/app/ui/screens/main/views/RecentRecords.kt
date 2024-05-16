package org.singing.app.ui.screens.main.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.baseline_navigate_next_24
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.domain.model.AccountUiData
import org.singing.app.domain.model.RecordData
import org.singing.app.ui.base.Space
import org.singing.app.ui.views.base.record.card.RecordCard
import org.singing.app.ui.views.shared.record.MainRecordCard
import org.singing.app.ui.views.shared.record.RecordCardActionsCallbacks


data class RecentRecordsData(
    val user: AccountUiData?,
    val records: List<RecordData>,
    val showMainRecord: Boolean,
)

data class RecentRecordsActions(
    val navigateAllRecords: () -> Unit,
    val navigateRecordDetails: (RecordData) -> Unit,
    val onPlayRecord: (RecordData) -> Unit,
    val onDeleteRecord: (RecordData) -> Unit,
)


@Composable
fun RecentRecords(
    gridModifier: Modifier,
    data: RecentRecordsData,
    actions: RecentRecordsActions,
    cardActions: RecordCardActionsCallbacks,
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Recent records",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
            )

            Spacer(Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(50))
                    .clickable {
                        actions.navigateAllRecords()
                    }
                    .padding(
                        start = 10.dp,
                        top = 2.dp,
                        end = 4.dp,
                        bottom = 2.dp,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "See all records",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge,
                )

                Icon(
                    imageVector = vectorResource(Res.drawable.baseline_navigate_next_24),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "",
                )
            }
        }

        Space(8.dp)

        if (data.records.isEmpty()) {
            // TODO:replace with EmptyView
            Text(
                text = "No records",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium,
            )
        } else {
            if (data.showMainRecord) {
                val mainItem = data.records.first()

                MainRecordCard(
                    record = mainItem,
                    creator = data.user,
                    cardActions = cardActions,
                    navigateRecordDetails = {
                        actions.navigateRecordDetails(mainItem)
                    },
                    playRecord = {
                        actions.onPlayRecord(mainItem)
                    },
                )
            }

            if (data.records.size > 1 || !data.showMainRecord) {
                if (data.showMainRecord) {
                    Space(12.dp)
                }

                RecordsGrid(gridModifier, data, actions)
            }
        }
    }
}

@Composable
private fun RecordsGrid(
    gridModifier: Modifier,
    data: RecentRecordsData,
    actions: RecentRecordsActions,
) {
    LazyVerticalGrid(
        modifier = gridModifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        columns = GridCells.Adaptive(
            minSize = 380.dp,
        ),
    ) {
        val offset = if (data.showMainRecord) 1 else 0

        items(data.records.size - offset) { index ->
            val item = data.records[index + offset]

            RecordCard(
                record = item,
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                accuracyContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                onClick = {
                    actions.navigateRecordDetails(item)
                }
            )
        }
    }
}
