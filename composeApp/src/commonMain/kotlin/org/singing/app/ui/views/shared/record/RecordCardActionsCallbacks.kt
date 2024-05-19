package org.singing.app.ui.views.shared.record

import androidx.compose.foundation.layout.height
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.domain.model.RecordData


private val chipHeight = Modifier.height(32.dp)

data class RecordCardActionsCallbacks(
    val onUploadRecord: (RecordData) -> Unit,
    val showPublication: (RecordData) -> Unit,
    val onPublishRecord: (RecordData) -> Unit,
    val onDeleteRecord: (RecordData) -> Unit,
)

@Composable
fun RecordCardActions(
    record: RecordData,
    actions: RecordCardActionsCallbacks,
) {
    if (!record.isSavedRemote) {
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
                actions.onUploadRecord(record)
            }
        )
    }

    if (record.isPublished) {
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
                actions.showPublication(record)
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
                actions.onPublishRecord(record)
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
            actions.onDeleteRecord(record)
        }
    )
}
