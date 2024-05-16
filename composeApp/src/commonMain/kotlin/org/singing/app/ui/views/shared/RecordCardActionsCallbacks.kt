package org.singing.app.ui.views.shared

import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.singing.app.composeapp.generated.resources.*
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.domain.model.RecordData


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
            label = {
                Text(
                    text = "Push to server",
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
            label = {
                Text(
                    text = "Show publication",
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
            label = {
                Text(
                    text = "Publish",
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
        label = {
            Text(
                text = "Delete record",
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
