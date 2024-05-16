package org.singing.app.ui.screens.main.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import com.singing.app.composeapp.generated.resources.*
import nl.jacobras.humanreadable.HumanReadable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.domain.model.AccountUiData
import org.singing.app.domain.model.RecordData
import org.singing.app.ui.base.Space
import org.singing.app.ui.theme.extended
import org.singing.app.ui.views.AppFilledButton
import org.singing.app.ui.views.IconLabel
import org.singing.app.ui.views.record.RecordCard
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds


data class RecentRecordsData(
    val user: AccountUiData?,
    val records: List<RecordData>,
    val showMainRecord: Boolean,
)

data class RecentRecordsActions(
    val navigateAllRecords: () -> Unit,
    val navigateRecordDetails: (RecordData) -> Unit,
    val onShowMistakes: (RecordData) -> Unit,
    val onDeleteRecord: (RecordData) -> Unit,
)


@Composable
fun RecentRecords(
    gridModifier: Modifier,
    data: RecentRecordsData,
    actions: RecentRecordsActions,
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
                MainRecordCard(data, actions)
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
private fun MainRecordCard(
    data: RecentRecordsData,
    actions: RecentRecordsActions,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.medium)
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
            .padding(all = 16.dp)
    ) {
        val mainItem = data.records.first()

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier.weight(1f)
            ) {
                AssistChip(
                    label = {
                        Text(
                            text = "Listen now",
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.labelLarge,
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = vectorResource(Res.drawable.baseline_play_circle_filled_24),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            contentDescription = "",
                        )
                    },
                    onClick = {}
                )

                Space(12.dp)

                AssistChip(
                    label = {
                        Text(
                            text = "Delete record",
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.labelLarge,
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = vectorResource(Res.drawable.baseline_delete_outline_24),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            contentDescription = "",
                        )
                    },
                    onClick = {
                        actions.onDeleteRecord(mainItem)
                    }
                )
            }

            Space(12.dp)

            Row(
                modifier = Modifier
                    .height(36.dp)
                    .clip(shape = RoundedCornerShape(50))
                    .background(color = MaterialTheme.colorScheme.surface),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = HumanReadable.timeAgo(mainItem.createdAt),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelLarge,
                )

                if (mainItem.isSavedRemote && data.user != null) {
                    Image(
                        modifier = Modifier
                            .size(size = 36.dp)
                            .clip(shape = RoundedCornerShape(50)),
                        painter = when (data.user.avatar) {
                            null -> painterResource(Res.drawable.baseline_person_24)
                            else -> rememberImagePainter(data.user.avatar)
                        },
                        contentScale = ContentScale.Crop,
                        contentDescription = "Avatar",
                    )
                }
            }
        }

        Space(12.dp)

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.medium)
                    .background(color = MaterialTheme.extended.secondaryFixedDim)
                    .padding(
                        horizontal = 48.dp,
                        vertical = 24.dp
                    )
            ) {
                val text = when (mainItem) {
                    is RecordData.Cover -> "${mainItem.accuracy}% accuracy"
                    is RecordData.Vocal -> "N/A"
                }

                Text(
                    text = text,
                    color = MaterialTheme.extended.onSecondaryFixedVariant,
                    style = MaterialTheme.typography.titleLarge,
                )
            }

            Space(12.dp)

            Column {
                IconLabel(
                    leadingIcon = vectorResource(Res.drawable.baseline_folder_music_black_24dp),
                    label = when (mainItem) {
                        is RecordData.Cover -> mainItem.filename
                        is RecordData.Vocal -> "No track selected"
                    }
                )

                Space(4.dp)

                IconLabel(
                    leadingIcon = vectorResource(Res.drawable.baseline_access_time_24),
                    label = HumanReadable.duration(
                        mainItem.duration.milliseconds
                            .inWholeSeconds.seconds
                    )
                )
            }
        }

        Space(12.dp)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = {
                    actions.navigateRecordDetails(mainItem)
                }
            ) {
                Text(
                    text = "See record",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelLarge,
                )
            }

            Space(12.dp)

            AppFilledButton(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                label = "View mistakes",
                onClick = {
                    actions.onShowMistakes(mainItem)
                }
            )
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
