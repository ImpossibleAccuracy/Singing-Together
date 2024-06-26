package com.singing.app.common.views.shared.record

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.singing.app.common.views.base.AppFilledButton
import com.singing.app.common.views.base.IconLabel
import com.singing.app.common.views.base.account.AccountChip
import com.singing.app.common.views.model.state.RecordUiData
import com.singing.app.common.views.model.state.UserUiData
import com.singing.app.common.views.views.generated.resources.Res
import com.singing.app.common.views.views.generated.resources.action_listen_now
import com.singing.app.common.views.views.generated.resources.action_see_record
import com.singing.app.common.views.views.generated.resources.baseline_access_time_24
import com.singing.app.common.views.views.generated.resources.baseline_folder_music_black_24dp
import com.singing.app.common.views.views.generated.resources.baseline_play_circle_filled_24
import com.singing.app.common.views.views.generated.resources.label_accuracy
import com.singing.app.common.views.views.generated.resources.label_no_selected_track_item
import com.singing.app.ui.screen.dimens
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
    availableActions: RecordCardActionsState,
    cardActions: RecordCardActionsCallbacks<RecordUiData>,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.medium)
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(all = MaterialTheme.dimens.dimen2),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1_5),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1_5)) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1_5),
            ) {
                FlowRow(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1_5),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1_5),
                ) {
                    RecordCardActions(
                        data = data,
                        state = availableActions,
                        actions = cardActions,
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1_5),
                ) {
                    Box(
                        modifier = Modifier
                            .clip(shape = MaterialTheme.shapes.medium)
                            .background(color = MaterialTheme.extended.primaryFixedDim)
                            .padding(
                                horizontal = MaterialTheme.dimens.dimen6,
                                vertical = MaterialTheme.dimens.dimen4
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
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen0_5),
                    ) {
                        IconLabel(
                            leadingIcon = vectorResource(Res.drawable.baseline_folder_music_black_24dp),
                            label = when (data.filename) {
                                null -> stringResource(Res.string.label_no_selected_track_item)
                                else -> data.filename
                            },
                        )

                        IconLabel(
                            leadingIcon = vectorResource(Res.drawable.baseline_access_time_24),
                            label = data.duration,
                        )
                    }
                }
            }

            AccountChip(
                username = data.createdAt,
                avatar = creator?.avatar,
                avatarAtStart = false,
            )
        }

        MainRecordActions(navigateRecordDetails, playRecord)
    }
}

@Composable
private fun MainRecordActions(navigateRecordDetails: () -> Unit, playRecord: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1_5, Alignment.End),
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
