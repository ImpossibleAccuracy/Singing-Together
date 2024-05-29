package com.singing.feature.recording.save.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.singing.app.common.views.base.RadioGroup
import com.singing.app.domain.model.RecordData
import com.singing.app.navigation.dialog.SkippableNavigationalDialogScreen
import com.singing.feature.recording.save.RecordSaveAdditionalInfo
import com.singing.feature.recording.save.RecordSaveStrategy
import kotlinx.collections.immutable.persistentListOf


data class SelectRecordStoreMethodScreen(
    val data: RecordSaveAdditionalInfo,
) : SkippableNavigationalDialogScreen<RecordData>() {
    private val saveRemote = mutableStateOf(true)

    override fun buildNextPage() = ProcessRecordSaveScreen(
        data = data,
        strategy = when (saveRemote.value) {
            true -> RecordSaveStrategy.Remote
            false -> RecordSaveStrategy.Locally
        }
    )

    @Composable
    override fun Content() {
        var saveRemoteState by remember { saveRemote }

        RadioGroup(
            items = persistentListOf(true, false),
            selectedItem = saveRemoteState,
            onSelectedItemChanged = { saveRemoteState = it },
            divider = {
                HorizontalDivider(
                    Modifier.padding(horizontal = 16.dp)
                )
            }
        ) { isSavedRemote ->
            Column(
                modifier = Modifier
                    .height(72.dp)
                    .padding(
                        vertical = 8.dp,
                        horizontal = 16.dp,
                    ),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = when (isSavedRemote) {
                        true -> "Push to server"
                        false -> "Save locally"
                    },
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                )

                Text(
                    text = when (isSavedRemote) {
                        true -> "Recording will be saved in your account"
                        false -> "Recording will be saved on current device"
                    },
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}
