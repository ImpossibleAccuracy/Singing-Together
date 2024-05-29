package com.singing.feature.recording.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.singing.app.common.views.base.AppFilledButton
import com.singing.app.ui.formatTimeString
import com.singing.app.ui.screen.dimens
import com.singing.feature.recording.presenter.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


@Composable
fun RecorderControls(
    modifier: Modifier = Modifier,
    isRecording: Boolean,
    canRecord: Boolean,
    recordDuration: Long,
    onRecordStart: () -> Unit,
    onRecordFinish: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(
                horizontal = MaterialTheme.dimens.dimen3,
                vertical = MaterialTheme.dimens.dimen2,
            )
    ) {
        if (isRecording) {
            AppFilledButton(
                label = stringResource(Res.string.action_stop_record),
                leadingIcon = vectorResource(Res.drawable.baseline_stop_black_24dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                onClick = onRecordFinish,
            )
        } else {
            AppFilledButton(
                enabled = canRecord,
                label = stringResource(Res.string.action_start_record),
                leadingIcon = vectorResource(Res.drawable.baseline_radio_button_checked_black_24dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                onClick = onRecordStart,
            )
        }

        Spacer(Modifier.weight(1f))

        if (isRecording) {
            Text(
                text = formatTimeString(recordDuration),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
            )
        } else {
            Text(
                text = stringResource(Res.string.label_start_record),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
