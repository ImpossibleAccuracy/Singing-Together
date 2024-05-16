package org.singing.app.ui.views.shared

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.singing.app.ui.views.base.AppTextButton

@Composable
fun DeleteRecordDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Delete record?")
        },
        text = {
            Text("Record cannot be restored after delete.")
        },
        confirmButton = {
            AppTextButton(
                contentColor = MaterialTheme.colorScheme.primary,
                label = "OK",
                onClick = onConfirm
            )
        },
        dismissButton = {
            AppTextButton(
                contentColor = MaterialTheme.colorScheme.primary,
                label = "Cancel",
                onClick = onDismiss
            )
        },
    )
}
