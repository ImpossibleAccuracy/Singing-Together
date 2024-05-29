package com.singing.feature.recording.save

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.model.UserData
import com.singing.app.domain.payload.RecordSaveData
import com.singing.app.navigation.dialog.FinalNavigationalDialogScreen
import com.singing.app.navigation.dialog.NavigationalDialog
import com.singing.domain.model.RecordPoint
import com.singing.feature.recording.presenter.generated.resources.*
import com.singing.feature.recording.save.items.RecordInfoScreen
import kotlinx.collections.immutable.PersistentList
import org.jetbrains.compose.resources.stringResource

data class RecordSaveAdditionalInfo(
    val saveData: RecordSaveData,
    val duration: Long,
    val user: UserData?,
    val history: PersistentList<RecordPoint>,
)


@Composable
fun RecordSaveDialog(
    data: RecordSaveAdditionalInfo,
    onSaved: (RecordData) -> Unit,
    onDismiss: () -> Unit,
) {
    NavigationalDialog(
        startScreen = {
            RecordInfoScreen(data)
        },
        backButtonText = { stringResource(Res.string.action_back) },
        dismissButtonText = { screen ->
            if (screen is FinalNavigationalDialogScreen) {
                stringResource(Res.string.action_close)
            } else {
                stringResource(Res.string.action_dismiss)
            }
        },
        confirmButtonText = { screen ->
            if (screen is FinalNavigationalDialogScreen) {
                stringResource(Res.string.action_view_record)
            } else {
                stringResource(Res.string.action_next)
            }
        },
        onFinish = onSaved,
        onDismiss = onDismiss,
    ) { dialogContent ->
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                text = "Record results",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 24.sp,
            )

            Text(
                text = "Please check your record data before proceeding.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Box(
            modifier = Modifier.heightIn(max = 450.dp)
        ) {
            dialogContent()
        }
    }
}
