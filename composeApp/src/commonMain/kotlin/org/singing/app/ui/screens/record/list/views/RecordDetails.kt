package org.singing.app.ui.screens.record.list.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.baseline_delete_outline_24
import com.singing.app.composeapp.generated.resources.baseline_file_upload_24
import nl.jacobras.humanreadable.HumanReadable
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.domain.model.AccountUiData
import org.singing.app.domain.model.RecordData
import org.singing.app.ui.base.Space
import org.singing.app.ui.base.formatTimeString
import org.singing.app.ui.views.account.AccountView


@Composable
fun RecordDetails(
    modifier: Modifier = Modifier,
    accountData: AccountUiData?,
    record: RecordData,
    onUploadRecord: () -> Unit,
    onDeleteRecord: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(
                horizontal = 24.dp,
                vertical = 16.dp
            )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(size = 128.dp)
                .clip(shape = MaterialTheme.shapes.medium)
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            Text(
                text = when (record) {
                    is RecordData.Cover -> "${record.accuracy}%"
                    is RecordData.Vocal -> "N/A"
                },
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
            )
        }

        Space(16.dp)

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = when (record) {
                    is RecordData.Cover -> record.filename
                    is RecordData.Vocal -> "No track selected"
                },
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
            )

            Space(2.dp)

            Text(
                text = formatTimeString(record.duration),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )

            Space(12.dp)

            Row {
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
                        onDeleteRecord()
                    }
                )

                if (!record.isSavedRemote) {
                    Space(12.dp)

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
                            onUploadRecord()
                        }
                    )
                }
            }
        }

        AccountView(
            username = HumanReadable.timeAgo(record.createdAt),
            avatar = accountData?.avatar?.let {
                rememberImagePainter(it)
            },
            showAvatar = accountData != null,
        )
    }
}
