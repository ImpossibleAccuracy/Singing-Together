package com.singing.feature.recording.save.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.singing.app.common.views.base.IconLabel
import com.singing.app.common.views.base.account.UserAvatar
import com.singing.app.domain.model.RecordData
import com.singing.app.navigation.dialog.FinalNavigationalDialogScreen
import com.singing.feature.recording.presenter.generated.resources.Res
import com.singing.feature.recording.presenter.generated.resources.label_saved_locally
import com.singing.feature.recording.presenter.generated.resources.label_saved_remote
import com.singing.feature.recording.save.RecordSaveAdditionalInfo
import com.singing.feature.recording.save.RecordSaveStrategy
import org.jetbrains.compose.resources.stringResource


data class RecordSavedScreen(
    val data: RecordSaveAdditionalInfo,
    val strategy: RecordSaveStrategy,
    val record: RecordData,
) : FinalNavigationalDialogScreen<RecordData?>() {
    override val result: RecordData
        get() = record

    @Composable
    override fun Content() {
        when (strategy) {
            RecordSaveStrategy.Locally -> {
                IconLabel(
                    leadingIcon = Icons.Filled.Warning,
                    label = stringResource(Res.string.label_saved_locally),
                )
            }

            is RecordSaveStrategy.Remote -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    UserAvatar(
                        size = 48.dp,
                        avatar = data.user?.avatar,
                    )

                    Text(
                        text = stringResource(Res.string.label_saved_remote, data.user!!.username),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}
