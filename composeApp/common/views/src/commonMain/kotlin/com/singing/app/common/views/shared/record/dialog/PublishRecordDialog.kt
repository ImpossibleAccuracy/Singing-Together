package com.singing.app.common.views.shared.record.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.singing.app.common.views.base.AppFilledButton
import com.singing.app.common.views.base.AppTextButton
import com.singing.app.common.views.views.generated.resources.*
import org.jetbrains.compose.resources.stringResource


@Composable
fun PublishRecordDialog(
    maxLength: Int,
    onConfirm: (description: String) -> Unit,
    onDismiss: () -> Unit,
) {
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(Res.string.title_dialog_record_publish)
            )
        },
        text = {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                label = {
                    Text(text = stringResource(Res.string.label_description))
                },
                supportingText = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "${description.length}/$maxLength",
                        textAlign = TextAlign.End,
                    )
                },
                value = description,
                onValueChange = {
                    if (it.length <= maxLength) {
                        description = it
                    }
                }
            )
        },
        confirmButton = {
            AppFilledButton(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                label = stringResource(Res.string.action_next),
                onClick = {
                    onConfirm(description)
                }
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
