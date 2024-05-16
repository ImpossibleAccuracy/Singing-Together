package org.singing.app.ui.screens.record.list.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.singing.app.domain.model.RecordData
import org.singing.app.ui.base.Space
import org.singing.app.ui.screens.record.list.RecordListScreen.Companion.PUBLICATION_DESCRIPTION_MAX_LENGTH
import org.singing.app.ui.screens.record.list.RecordListViewModel
import org.singing.app.ui.views.AppFilledButton
import org.singing.app.ui.views.AppTextButton

internal data class PublicationTabData(
    val record: RecordData,
    val description: String = "",
)

@Composable
internal fun PublicationTab(
    viewModel: RecordListViewModel,
    data: PublicationTabData,
    onDataUpdate: (PublicationTabData) -> Unit,
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Publish your record so that other users can estimate your result.",
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center,
    )

    Spacer(Modifier.height(24.dp))

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        singleLine = false,
        label = {
            Text(text = "Description")
        },
        supportingText = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "${data.description.length}/$PUBLICATION_DESCRIPTION_MAX_LENGTH",
                textAlign = TextAlign.End,
            )
        },
        value = data.description,
        onValueChange = {
            if (it.length <= PUBLICATION_DESCRIPTION_MAX_LENGTH) {
                onDataUpdate(
                    data.copy(
                        description = it
                    )
                )
            }
        }
    )

    Spacer(Modifier.height(24.dp))

    Row {
        AppTextButton(
            modifier = Modifier.widthIn(min = 180.dp),
            contentColor = MaterialTheme.colorScheme.tertiary,
            label = "Show preview",
            onClick = {

            }
        )

        Space(12.dp)

        AppFilledButton(
            modifier = Modifier.widthIn(min = 180.dp),
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary,
            label = "Publish",
            onClick = {
                viewModel.publishRecord(
                    data.record,
                    data.description,
                )
            }
        )
    }
}
