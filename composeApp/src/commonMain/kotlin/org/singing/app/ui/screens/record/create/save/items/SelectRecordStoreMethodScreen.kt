package org.singing.app.ui.screens.record.create.save.items

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
import kotlinx.collections.immutable.persistentListOf
import org.singing.app.domain.repository.record.RecordSaveData
import org.singing.app.domain.store.account.UserContainer
import org.singing.app.ui.screens.record.create.save.SkippableRecordSaveDialogScreen
import org.singing.app.ui.views.base.RadioGroup

data class SelectRecordStoreMethodScreen(
    val saveData: RecordSaveData,
) : SkippableRecordSaveDialogScreen() {
    private val saveRemote = mutableStateOf(true)

    override fun buildNextPage() = ProcessRecordSaveScreen(
        saveData = saveData,
        strategy = when (saveRemote.value) {
            true -> RecordSaveStrategy.Remote(
                account = UserContainer.user.value!!,
            )

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
