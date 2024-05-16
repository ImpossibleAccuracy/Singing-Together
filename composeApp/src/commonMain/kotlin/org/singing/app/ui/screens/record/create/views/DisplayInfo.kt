package org.singing.app.ui.screens.record.create.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.ui.base.formatTimeString
import org.singing.app.ui.views.base.AppFilledButton


@Composable
fun DisplayInfo(
    isRecording: Boolean,
    canRecord: Boolean,
    recordDuration: Long,
    onRecordStart: () -> Unit,
    onRecordFinish: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(
                horizontal = 24.dp,
                vertical = 16.dp
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
