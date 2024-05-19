package org.singing.app.ui.screens.record.create.save

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.action_close
import com.singing.app.composeapp.generated.resources.action_view_record
import com.singing.app.composeapp.generated.resources.baseline_person_24
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.singing.app.domain.model.RecordData
import org.singing.app.ui.screens.record.create.save.items.RecordSaveStrategy
import org.singing.app.ui.views.base.IconLabel
import org.singing.app.ui.views.base.account.rememberAvatarPainter


data class RecordSavedScreen(
    val record: RecordData,
    val info: RecordSaveStrategy,
) : RecordSaveDialogScreen {
    @Composable
    override fun confirmText(): String {
        return stringResource(Res.string.action_view_record)
    }

    @Composable
    override fun dismissText(): String {
        return stringResource(Res.string.action_close)
    }

    @Composable
    override fun Content() {
        when (info) {
            RecordSaveStrategy.Locally -> {
                IconLabel(
                    leadingIcon = Icons.Filled.Warning,
                    label = "Saved to your device"
                )
            }

            is RecordSaveStrategy.Remote -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Image(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(shape = RoundedCornerShape(50)),
                        painter = rememberAvatarPainter(info.account.avatar),
                        contentScale = ContentScale.Crop,
                        contentDescription = "",
                    )

                    Text(
                        text = "Saved to ${info.account.username} account",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}
