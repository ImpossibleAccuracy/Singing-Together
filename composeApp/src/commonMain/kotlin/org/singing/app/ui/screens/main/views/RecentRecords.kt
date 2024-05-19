package org.singing.app.ui.screens.main.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.*
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.domain.model.AccountUiData
import org.singing.app.domain.model.RecordData
import org.singing.app.ui.base.cardAppearance
import org.singing.app.ui.views.base.list.EmptyView
import org.singing.app.ui.views.base.record.RecordCard
import org.singing.app.ui.views.shared.record.MainRecordCard
import org.singing.app.ui.views.shared.record.RecordCardActionsCallbacks


data class RecentRecordsData(
    val user: AccountUiData?,
    val records: ImmutableList<RecordData>,
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
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (data.records.isEmpty()) {
            EmptyView(
                modifier = Modifier
                    .fillMaxWidth()
                    .cardAppearance(
                        shape = MaterialTheme.shapes.small,
                        background = MaterialTheme.colorScheme.primaryContainer,
                        padding = PaddingValues(24.dp)
                    ),
                icon = {
                    Icon(
                        modifier = Modifier.size(72.dp),
                        imageVector = vectorResource(Res.drawable.baseline_volume_up_black_24dp),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = "",
                    )
                },
                title = stringResource(Res.string.title_empty_records),
                subtitle = stringResource(Res.string.subtitle_empty_records),
            )
        } else {
            RecentRecordsHeader(actions)

            RecordsGrid(
                gridModifier = gridModifier,
                data = data,
                actions = actions,
                cardActions = cardActions,
            )
        }
    }
}

@Composable
private fun RecentRecordsHeader(
    actions: RecentRecordsActions,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.title_recent_records),
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
                text = stringResource(Res.string.action_see_all_record),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelLarge,
            )

            Icon(
                imageVector = vectorResource(Res.drawable.baseline_navigate_next_24),
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = "",
            )
        }
    }
}

@Composable
private fun RecordsGrid(
    gridModifier: Modifier,
    data: RecentRecordsData,
    actions: RecentRecordsActions,
    cardActions: RecordCardActionsCallbacks,
) {
    LazyVerticalStaggeredGrid(
        modifier = gridModifier,
        verticalItemSpacing = 12.dp,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        columns = StaggeredGridCells.Adaptive(
            minSize = 380.dp,
        ),
    ) {
        if (data.showMainRecord) {
            item(span = StaggeredGridItemSpan.FullLine) {
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
        }

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
