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
import com.singing.feature.recording.presenter.generated.resources.*
import com.singing.feature.recording.presenter.generated.resources.Res
import com.singing.feature.recording.presenter.generated.resources.action_push_to_server
import com.singing.feature.recording.presenter.generated.resources.action_save_locally
import com.singing.feature.recording.presenter.generated.resources.label_push_to_server
import com.singing.feature.recording.save.RecordSaveAdditionalInfo
import com.singing.feature.recording.save.RecordSaveStrategy
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource


data class SelectRecordStoreMethodScreen(
    val data: RecordSaveAdditionalInfo,
) : SkippableNavigationalDialogScreen<RecordData?>() {
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
                        true -> stringResource(Res.string.action_push_to_server)
                        false -> stringResource(Res.string.action_save_locally)
                    },
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                )

                Text(
                    text = when (isSavedRemote) {
                        true -> stringResource(Res.string.label_push_to_server)
                        false -> stringResource(Res.string.label_save_locally)
                    },
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}
