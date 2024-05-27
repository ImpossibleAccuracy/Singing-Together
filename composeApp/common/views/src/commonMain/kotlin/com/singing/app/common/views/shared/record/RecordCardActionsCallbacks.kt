package com.singing.app.common.views.shared.record

import androidx.compose.foundation.layout.height
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.singing.app.common.views.model.state.RecordUiData
import com.singing.app.common.views.views.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


private val chipHeight = Modifier.height(32.dp)

data class RecordCardActionsCallbacks<T>(
    val onUploadRecord: (T) -> Unit,
    val showPublication: (T) -> Unit,
    val onPublishRecord: (T) -> Unit,
    val onDeleteRecord: (T) -> Unit,
)

@Composable
fun RecordCardActions(
    data: RecordUiData,
    actions: RecordCardActionsCallbacks<RecordUiData>,
) {
    if (!data.isSavedRemote) {
        AssistChip(
            modifier = chipHeight,
            label = {
                Text(
                    text = stringResource(Res.string.action_upload_record),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelLarge,
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = vectorResource(Res.drawable.baseline_file_upload_24),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "",
                )
            },
            onClick = {
                actions.onUploadRecord(data)
            }
        )
    }

    if (data.isPublished) {
        AssistChip(
            modifier = chipHeight,
            label = {
                Text(
                    text = stringResource(Res.string.action_show_publication),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelLarge,
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = vectorResource(Res.drawable.baseline_open_in_new_24),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "",
                )
            },
            onClick = {
                actions.showPublication(data)
            }
        )
    } else {
        AssistChip(
            modifier = chipHeight,
            label = {
                Text(
                    text = stringResource(Res.string.action_publish_record),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelLarge,
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = vectorResource(Res.drawable.baseline_data_saver_on_24),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "",
                )
            },
            onClick = {
                actions.onPublishRecord(data)
            }
        )
    }

    AssistChip(
        modifier = chipHeight,
        label = {
            Text(
                text = stringResource(Res.string.action_delete_record),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelLarge,
            )
        },
        trailingIcon = {
            Icon(
                imageVector = vectorResource(Res.drawable.baseline_delete_outline_24),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = "",
            )
        },
        onClick = {
            actions.onDeleteRecord(data)
        }
    )
}
