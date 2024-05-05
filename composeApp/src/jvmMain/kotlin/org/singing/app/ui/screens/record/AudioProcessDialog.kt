package org.singing.app.ui.screens.record

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.singing.app.ui.helper.Space
import org.singing.app.ui.screens.record.model.RecordItem

data class AudioProcessDialogData(
    val isParsing: Boolean,
    val title: String,
    val result: List<RecordItem>? = null,
)

@Composable
fun AudioProcessDialog(
    visible: Boolean,
    onCloseRequest: () -> Unit = {},
    data: AudioProcessDialogData,
) {
    if (visible) {
        Dialog(
            onDismissRequest = onCloseRequest,
        ) {
            Box(
                modifier = Modifier
                    .widthIn(min = 500.dp)
                    .heightIn(min = 300.dp)
                    .padding(24.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp, 12.dp),
            ) {
                if (data.isParsing) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                    )
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = data.title,
                            style = MaterialTheme.typography.titleLarge,
                        )

                        Space(12.dp)

                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(data.result!!.size) { index ->
                                val item = data.result[index]

                                Text(
                                    text = item.toString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                )

                                if (index != data.result.lastIndex) {
                                    Space(8.dp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
