package org.singing.app.ui.screens.record.details.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import nl.jacobras.humanreadable.HumanReadable
import org.singing.app.domain.model.AccountUiData
import org.singing.app.domain.model.RecordData
import org.singing.app.ui.base.Space
import org.singing.app.ui.base.formatTimeString
import org.singing.app.ui.views.base.account.AccountChip
import org.singing.app.ui.views.base.account.rememberAvatarPainter
import org.singing.app.ui.views.shared.record.RecordCardActions
import org.singing.app.ui.views.shared.record.RecordCardActionsCallbacks
import org.singing.app.ui.views.shared.record.RecordThumb


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
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        RecordThumb(
            size = 128.dp,
            textStyle = MaterialTheme.typography.titleLarge,
            accuracy = when (record) {
                is RecordData.Cover -> record.accuracy
                is RecordData.Vocal -> null
            }
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            val avatar = rememberAvatarPainter(accountData?.avatar)

            FlowRow(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                RecordInfo(
                    modifier = Modifier.fillMaxWidth(),
                    record = record,
                )

                AccountChip(
                    username = HumanReadable.timeAgo(record.createdAt.instant),
                    avatar = { avatar }
                )
            }

            if (actions != null) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
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
}

@Composable
private fun RecordInfo(
    modifier: Modifier,
    record: RecordData,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = when (record) {
                is RecordData.Cover -> record.filename
                is RecordData.Vocal -> "No track selected"
            },
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
        )

        Space(2.dp)

        Text(
            text = formatTimeString(record.duration),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

