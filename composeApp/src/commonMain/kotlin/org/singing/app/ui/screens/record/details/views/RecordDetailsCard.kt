package org.singing.app.ui.screens.record.details.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import nl.jacobras.humanreadable.HumanReadable
import org.singing.app.domain.model.AccountUiData
import org.singing.app.domain.model.RecordData
import org.singing.app.ui.base.Space
import org.singing.app.ui.base.formatTimeString
import org.singing.app.ui.views.base.account.AccountView
import org.singing.app.ui.views.shared.record.RecordCardActions
import org.singing.app.ui.views.shared.record.RecordCardActionsCallbacks


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecordDetailsCard(
    modifier: Modifier = Modifier,
    accountData: AccountUiData?,
    record: RecordData,
    actions: RecordCardActionsCallbacks?,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(
                horizontal = 24.dp,
                vertical = 16.dp
            ),
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
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

                if (actions != null) {
                    Space(12.dp)

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        RecordCardActions(
                            record = record,
                            actions = actions,
                        )
                    }
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

