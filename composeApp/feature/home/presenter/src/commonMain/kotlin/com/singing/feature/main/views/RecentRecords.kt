package com.singing.feature.main.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.singing.app.common.views.base.list.EmptyView
import com.singing.app.common.views.base.record.RecordCard
import com.singing.app.common.views.shared.record.MainRecordCard
import com.singing.app.common.views.shared.record.RecordCardActionsCallbacks
import com.singing.app.common.views.shared.record.dialog.DeleteRecordDialog
import com.singing.app.common.views.shared.record.dialog.PublishRecordDialog
import com.singing.app.common.views.shared.record.dialog.RecordPlayDialog
import com.singing.app.common.views.toPlayerController
import com.singing.app.common.views.toRecordCardData
import com.singing.app.common.views.toUserUiData
import com.singing.app.domain.model.MAX_PUBLICATION_DESCRIPTION_LENGTH
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.model.UserData
import com.singing.app.feature.rememberRecordCardActions
import com.singing.app.feature.rememberRecordPlayer
import com.singing.app.navigation.SharedScreen
import com.singing.app.ui.screen.dimens
import com.singing.app.ui.utils.cardAppearance
import com.singing.feature.main.presenter.generated.resources.Res
import com.singing.feature.main.presenter.generated.resources.action_see_all_record
import com.singing.feature.main.presenter.generated.resources.baseline_navigate_next_24
import com.singing.feature.main.presenter.generated.resources.baseline_volume_up_black_24dp
import com.singing.feature.main.presenter.generated.resources.subtitle_empty_records
import com.singing.feature.main.presenter.generated.resources.title_empty_records
import com.singing.feature.main.presenter.generated.resources.title_recent_records
import com.singing.feature.main.viewmodel.MainIntent
import com.singing.feature.main.viewmodel.MainUiState
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


data class RecentRecordsData(
    val user: UserData?,
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
fun RecentRecordsContainer(
    uiState: MainUiState,
    newIntent: (MainIntent) -> Unit,
    navigateToRecordPublication: (RecordData) -> Unit,
    navigate: (SharedScreen) -> Unit
) {
    var showMainRecord by rememberSaveable { mutableStateOf(true) }

    var recordToDelete by remember { mutableStateOf<RecordData?>(null) }
    var recordToPublish by remember { mutableStateOf<RecordData?>(null) }
    var recordToPlay by remember { mutableStateOf<RecordData?>(null) }

    if (recordToPlay != null) {
        val dialogRecordPlayer = rememberRecordPlayer()

        RecordPlayDialog(
            playerController = dialogRecordPlayer.toPlayerController(recordToPlay!!),
            record = recordToPlay!!.toRecordCardData(),
            creator = uiState.user?.toUserUiData(),
            onDismiss = {
                recordToPlay = null
            }
        )
    }

    RecentRecords(
        data = RecentRecordsData(
            user = uiState.user,
            records = uiState.records,
            showMainRecord = showMainRecord,
        ),
        actions = RecentRecordsActions(
            navigateAllRecords = {
                navigate(SharedScreen.RecordList())
            },
            navigateRecordDetails = {
                navigate(SharedScreen.RecordList(it))
            },
            onPlayRecord = {
                recordToPlay = it
            },
            onDeleteRecord = {
                recordToDelete = it
            }
        ),
        cardActions = RecordCardActionsCallbacks(
            onUploadRecord = {
                newIntent(MainIntent.UploadRecord(it))
            },
            showPublication = {
                navigateToRecordPublication(it)
            },
            onPublishRecord = {
                recordToPublish = it
            },
            onDeleteRecord = {
                recordToDelete = it
            },
        )
    )

    if (recordToDelete != null) {
        DeleteRecordDialog(
            onConfirm = {
                newIntent(MainIntent.DeleteRecord(recordToDelete!!))

                showMainRecord = false
                recordToDelete = null
            },
            onDismiss = {
                recordToDelete = null
            }
        )
    }

    if (recordToPublish != null) {
        PublishRecordDialog(
            maxLength = MAX_PUBLICATION_DESCRIPTION_LENGTH,
            onConfirm = {
                newIntent(MainIntent.PublishRecord(recordToPublish!!, it))

                recordToPublish = null
            },
            onDismiss = {
                recordToPublish = null
            },
        )
    }
}


@Composable
fun RecentRecords(
    modifier: Modifier = Modifier,
    gridModifier: Modifier = Modifier,
    data: RecentRecordsData,
    actions: RecentRecordsActions,
    cardActions: RecordCardActionsCallbacks<RecordData>,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1),
    ) {
        if (data.records.isEmpty()) {
            EmptyView(
                modifier = Modifier
                    .fillMaxWidth()
                    .cardAppearance(
                        shape = MaterialTheme.shapes.small,
                        background = MaterialTheme.colorScheme.primaryContainer,
                        padding = PaddingValues(MaterialTheme.dimens.dimen3)
                    ),
                icon = {
                    Icon(
                        modifier = Modifier.size(MaterialTheme.dimens.dimen4_5 * 2),
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
                    start = MaterialTheme.dimens.dimen1,
                    top = MaterialTheme.dimens.dimen0_25,
                    end = MaterialTheme.dimens.dimen0_5,
                    bottom = MaterialTheme.dimens.dimen0_25,
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
    cardActions: RecordCardActionsCallbacks<RecordData>,
) {
    LazyVerticalStaggeredGrid(
        modifier = gridModifier,
        verticalItemSpacing = MaterialTheme.dimens.dimen1_5,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1_5),
        columns = StaggeredGridCells.Adaptive(
            minSize = 380.dp,
        ),
    ) {
        if (data.showMainRecord) {
            item(span = StaggeredGridItemSpan.FullLine) {
                val mainItem = data.records.first()

                MainRecordCard(
                    data = mainItem.toRecordCardData(),
                    creator = data.user?.toUserUiData(),
                    cardActions = RecordCardActionsCallbacks(
                        onUploadRecord = {
                            cardActions.onUploadRecord(mainItem)
                        },
                        showPublication = {
                            cardActions.showPublication(mainItem)
                        },
                        onPublishRecord = {
                            cardActions.onPublishRecord(mainItem)
                        },
                        onDeleteRecord = {
                            cardActions.onDeleteRecord(mainItem)
                        },
                    ),
                    navigateRecordDetails = {
                        actions.navigateRecordDetails(mainItem)
                    },
                    availableActions = rememberRecordCardActions(data.user, mainItem),
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
                data = item.toRecordCardData(),
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                accuracyContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                onClick = {
                    actions.navigateRecordDetails(item)
                }
            )
        }
    }
}
