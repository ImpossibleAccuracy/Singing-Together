package com.singing.app.common.views.shared.record.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.dokar.chiptextfield.Chip
import com.dokar.chiptextfield.rememberChipTextFieldState
import com.singing.app.common.views.base.AppChipTextField
import com.singing.app.common.views.base.AppFilledButton
import com.singing.app.common.views.base.AppTextButton
import com.singing.app.common.views.views.generated.resources.*
import com.singing.app.ui.screen.dimens
import org.jetbrains.compose.resources.stringResource


@Composable
fun PublishRecordDialog(
    maxLength: Int,
    onConfirm: (description: String, tags: List<String>) -> Unit,
    onDismiss: () -> Unit,
) {
    var description by remember { mutableStateOf("") }
    var tagsValue by remember { mutableStateOf("") }
    val tagsList = rememberChipTextFieldState<Chip>()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(Res.string.title_dialog_record_publish)
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen0_5)) {
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

                AppChipTextField(
                    label = "Publication tags",
                    value = tagsValue,
                    state = tagsList,
                    onValueChange = {
                        tagsValue = it
                    },
                    onSubmit = {
                        tagsValue = ""

                        Chip(it)
                    },
                    onRemove = {
                        tagsList.removeChip(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = Color.Transparent,
                )
            }
        },
        confirmButton = {
            AppFilledButton(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                label = stringResource(Res.string.action_next),
                onClick = {
                    val resultDescription = description.trim()
                    val resultTags = tagsList.chips.map { it.text }
                        .plus(tagsValue)
                        .map { it.trim() }
                        .filter { it.isNotBlank() }
                        .distinct()

                    onConfirm(
                        resultDescription,
                        resultTags,
                    )
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
