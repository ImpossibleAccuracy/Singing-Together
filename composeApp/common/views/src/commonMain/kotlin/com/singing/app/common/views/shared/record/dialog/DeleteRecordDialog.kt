package com.singing.app.common.views.shared.record.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.singing.app.common.views.base.AppTextButton
import com.singing.app.common.views.views.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun DeleteRecordDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(Res.string.title_dialog_record_delete)
            )
        },
        text = {
            Text(
                text = stringResource(Res.string.subtitle_dialog_record_delete)
            )
        },
        confirmButton = {
            AppTextButton(
                contentColor = MaterialTheme.colorScheme.primary,
                label = stringResource(Res.string.action_confirm),
                onClick = onConfirm
            )
        },
        dismissButton = {
            AppTextButton(
                contentColor = MaterialTheme.colorScheme.primary,
                label = stringResource(Res.string.action_dismiss),
                onClick = onDismiss
            )
        },
    )
}
