package org.singing.app.ui.views.shared.record

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.*
import nl.jacobras.humanreadable.HumanReadable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.domain.model.AccountUiData
import org.singing.app.domain.model.RecordData
import org.singing.app.ui.base.Space
import org.singing.app.ui.theme.extended
import org.singing.app.ui.views.base.AppFilledButton
import org.singing.app.ui.views.base.IconLabel
import org.singing.app.ui.views.base.account.AccountChip
import org.singing.app.ui.views.base.account.rememberAvatarPainter
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainRecordCard(
    modifier: Modifier = Modifier,
    record: RecordData,
    creator: AccountUiData?,
    playRecord: () -> Unit,
    navigateRecordDetails: () -> Unit,
    cardActions: RecordCardActionsCallbacks,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.medium)
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(all = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            FlowRow(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                RecordCardActions(
                    record = record,
                    actions = cardActions,
                )
            }

            val avatar = rememberAvatarPainter(creator?.avatar)

            AccountChip(
                username = HumanReadable.timeAgo(record.createdAt.instant),
                avatar = { avatar },
                avatarAtStart = false,
            )
        }

        Space(12.dp)

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.medium)
                    .background(color = MaterialTheme.extended.primaryFixedDim)
                    .padding(
                        horizontal = 48.dp,
                        vertical = 24.dp
                    )
            ) {
                val text = when (record) {
                    is RecordData.Cover -> stringResource(Res.string.label_accuracy, record.accuracy)
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
                    label = when (record) {
                        is RecordData.Cover -> record.name
                        is RecordData.Vocal -> "No track selected"
                    }
                )

                Space(4.dp)

                IconLabel(
                    leadingIcon = vectorResource(Res.drawable.baseline_access_time_24),
                    label = HumanReadable.duration(
                        record.duration.milliseconds
                            .inWholeSeconds.seconds
                    )
                )
            }
        }

        Space(12.dp)

        MainRecordActions(navigateRecordDetails, playRecord)
    }
}

@Composable
private fun MainRecordActions(navigateRecordDetails: () -> Unit, playRecord: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        TextButton(
            onClick = {
                navigateRecordDetails()
            }
        ) {
            Text(
                text = stringResource(Res.string.action_see_record),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge,
            )
        }

        Space(12.dp)

        AppFilledButton(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            label = stringResource(Res.string.action_listen_now),
            trailingIcon = vectorResource(Res.drawable.baseline_play_circle_filled_24),
            onClick = {
                playRecord()
            }
        )
    }
}