package org.singing.app.ui.views.shared

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import org.singing.app.ui.screens.record.list.RecordListScreen.Companion.PUBLICATION_DESCRIPTION_MAX_LENGTH
import org.singing.app.ui.views.base.AppFilledButton
import org.singing.app.ui.views.base.AppTextButton


@Composable
fun PublishRecordDialog(
    onConfirm: (description: String) -> Unit,
    onDismiss: () -> Unit,
) {
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Enter description")
        },
        text = {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                label = {
                    Text(text = "Description")
                },
                supportingText = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "${description.length}/$PUBLICATION_DESCRIPTION_MAX_LENGTH",
                        textAlign = TextAlign.End,
                    )
                },
                value = description,
                onValueChange = {
                    if (it.length <= PUBLICATION_DESCRIPTION_MAX_LENGTH) {
                        description = it
                    }
                }
            )
        },
        confirmButton = {
            AppFilledButton(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                label = "Next",
                onClick = {
                    onConfirm(description)
                }
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
