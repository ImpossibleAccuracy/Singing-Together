package com.singing.feature.record.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.singing.app.common.views.base.account.AccountChip
import com.singing.app.common.views.shared.record.RecordCardActions
import com.singing.app.common.views.shared.record.RecordCardActionsCallbacks
import com.singing.app.common.views.shared.record.RecordCardActionsState
import com.singing.app.common.views.shared.record.RecordThumb
import com.singing.app.common.views.shared.record.dialog.DeleteRecordDialog
import com.singing.app.common.views.shared.record.dialog.PublishRecordDialog
import com.singing.app.common.views.toRecordCardData
import com.singing.app.domain.model.MAX_PUBLICATION_DESCRIPTION_LENGTH
import com.singing.app.domain.model.RecordData
import com.singing.app.ui.formatTimeString
import com.singing.app.ui.screen.dimens
import com.singing.feature.record.views.generated.resources.Res
import com.singing.feature.record.views.generated.resources.label_no_selected_track_item
import nl.jacobras.humanreadable.HumanReadable
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun RecordDetailsMain(
    modifier: Modifier = Modifier,
    data: RecordDetailsData,
    actions: RecordDetailsActions?,
    availableActions: RecordCardActionsState,
) {
    var recordToPublish by remember { mutableStateOf<RecordData?>(null) }
    var recordToDelete by remember { mutableStateOf<RecordData?>(null) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .padding(
                horizontal = MaterialTheme.dimens.dimen3,
                vertical = MaterialTheme.dimens.dimen2,
            ),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen2),
    ) {
        RecordThumb(
            size = MaterialTheme.dimens.dimen5_5 * 3,
            textStyle = MaterialTheme.typography.titleLarge,
            accuracy = when (data.record) {
                is RecordData.Cover -> data.record.accuracy
                is RecordData.Vocal -> null
            }
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1_5),
        ) {
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1_5),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1_5),
            ) {
                RecordInfo(
                    modifier = Modifier.fillMaxWidth(),
                    record = data.record,
                )

                AccountChip(
                    username = HumanReadable.timeAgo(data.record.createdAt.instant),
                    avatar = data.user?.avatar
                )
            }

            if (actions != null) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1_5),
                ) {
                    RecordCardActions(
                        data = data.record.toRecordCardData(),
                        state = availableActions,
                        actions = RecordCardActionsCallbacks(
                            onUploadRecord = {
                                actions.uploadRecord?.invoke()
                            },
                            showPublication = {
                                actions.navigatePublication?.invoke()
                            },
                            onPublishRecord = {
                                recordToPublish = data.record
                            },
                            onDeleteRecord = {
                                recordToDelete = data.record
                            },
                        ),
                    )
                }
            }
        }
    }

    if (actions?.publishRecord != null && availableActions.canPublish) {
        PublishDialog(
            record = recordToPublish,
            publishRecord = actions.publishRecord,
            onDismiss = { recordToPublish = null },
        )
    }

    if (actions?.deleteRecord != null && availableActions.canDelete) {
        DeleteDialog(
            record = recordToDelete,
            deleteRecord = actions.deleteRecord,
            onDismiss = { recordToDelete = null }
        )
    }
}

@Composable
private fun PublishDialog(
    record: RecordData?,
    publishRecord: (String, List<String>) -> Unit,
    onDismiss: () -> Unit,
) {
    if (record != null) {
        PublishRecordDialog(
            maxLength = MAX_PUBLICATION_DESCRIPTION_LENGTH,
            onConfirm = { description, tags ->
                publishRecord(description, tags)

                onDismiss()
            },
            onDismiss = onDismiss,
        )
    }
}

@Composable
private fun DeleteDialog(
    record: RecordData?,
    deleteRecord: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (record != null) {
        DeleteRecordDialog(
            onConfirm = {
                deleteRecord()

                onDismiss()
            },
            onDismiss = onDismiss,
        )
    }
}

@Composable
private fun RecordInfo(
    modifier: Modifier,
    record: RecordData,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen0_25)
    ) {
        Text(
            text = record.name ?: stringResource(Res.string.label_no_selected_track_item),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
        )

        Text(
            text = formatTimeString(record.duration),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

