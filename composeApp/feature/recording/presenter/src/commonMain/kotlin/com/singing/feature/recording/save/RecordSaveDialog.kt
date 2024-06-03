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
import com.singing.app.ui.screen.dimens
import com.singing.domain.model.RecordPoint
import com.singing.feature.recording.presenter.generated.resources.*
import com.singing.feature.recording.save.items.RecordInfoScreen
import com.singing.feature.recording.save.items.RecordSaveErrorScreen
import com.singing.feature.recording.save.items.RecordSavedScreen
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
    onError: () -> Unit,
    onDismiss: () -> Unit,
) {
    NavigationalDialog(
        startScreen = {
            RecordInfoScreen(data)
        },
        backButtonText = { stringResource(Res.string.action_back) },
        dismissButtonText = { screen ->
            if (screen is RecordSavedScreen) {
                stringResource(Res.string.action_close)
            } else {
                stringResource(Res.string.action_dismiss)
            }
        },
        confirmButtonText = { screen ->
            if (screen is RecordSavedScreen) {
                stringResource(Res.string.action_view_record)
            } else if (screen is RecordSaveErrorScreen) {
                stringResource(Res.string.action_confirm)
            } else {
                stringResource(Res.string.action_next)
            }
        },
        onFinish = {
            if (it == null) {
                onError()
            } else {
                onSaved(it)
            }
        },
        onDismiss = onDismiss,
    ) { screen, dialogContent ->
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen2)) {
            val title = when (screen) {
                is RecordSaveErrorScreen -> stringResource(Res.string.error_save_record_title)
                is RecordSavedScreen -> stringResource(Res.string.title_save_record_done)
                else -> stringResource(Res.string.title_save_record)
            }

            val subtitle = when (screen) {
                is RecordSaveErrorScreen -> stringResource(Res.string.error_save_record_subtitle)
                is RecordSavedScreen -> null
                else -> stringResource(Res.string.subtitle_save_record)
            }

            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 24.sp,
            )

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        Box(modifier = Modifier.heightIn(max = 450.dp)) {
            dialogContent()
        }
    }
}
