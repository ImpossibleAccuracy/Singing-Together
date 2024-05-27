package com.singing.app.common.views.shared.record

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
import com.singing.app.common.views.base.AppFilledButton
import com.singing.app.common.views.base.IconLabel
import com.singing.app.common.views.base.account.AccountChip
import com.singing.app.common.views.model.state.RecordUiData
import com.singing.app.common.views.model.state.UserUiData
import com.singing.app.common.views.views.generated.resources.*
import com.singing.app.ui.theme.extended.extended
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainRecordCard(
    modifier: Modifier = Modifier,
    data: RecordUiData,
    creator: UserUiData?,
    playRecord: () -> Unit,
    navigateRecordDetails: () -> Unit,
    cardActions: RecordCardActionsCallbacks<RecordUiData>,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.medium)
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
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
                    data = data,
                    actions = cardActions,
                )
            }

            AccountChip(
                username = data.createdAt,
                avatar = creator?.avatar,
                avatarAtStart = false,
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
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
                val text = when (data.accuracy) {
                    null -> "N/A"
                    else -> stringResource(
                        Res.string.label_accuracy,
                        data.accuracy
                    )
                }

                Text(
                    text = text,
                    color = MaterialTheme.extended.onSecondaryFixedVariant,
                    style = MaterialTheme.typography.titleLarge,
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                IconLabel(
                    leadingIcon = vectorResource(Res.drawable.baseline_folder_music_black_24dp),
                    label = when (data.filename) {
                        null -> stringResource(Res.string.label_no_track_selected)
                        else -> data.filename
                    },
                )

                IconLabel(
                    leadingIcon = vectorResource(Res.drawable.baseline_access_time_24),
                    label = data.duration,
                )
            }
        }

        MainRecordActions(navigateRecordDetails, playRecord)
    }
}

@Composable
private fun MainRecordActions(navigateRecordDetails: () -> Unit, playRecord: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
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
