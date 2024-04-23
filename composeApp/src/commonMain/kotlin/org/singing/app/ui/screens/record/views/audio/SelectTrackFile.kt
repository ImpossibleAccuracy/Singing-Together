package org.singing.app.ui.screens.record.views.audio

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.baseline_folder_open_black_24dp
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.ui.helper.Space

@Composable
fun SelectTrackFile(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onTrackPickup: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 8.dp,
            )
    ) {
        Icon(
            modifier = Modifier.size(size = 28.dp),
            imageVector = vectorResource(Res.drawable.baseline_folder_open_black_24dp),
            contentDescription = "folder-play",
            tint = MaterialTheme.colorScheme.onTertiaryContainer,
        )

        Space(12.dp)

        Text(
            modifier = Modifier.weight(1f),
            text = "Выберите трек для сравнения с голосом",
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            style = MaterialTheme.typography.bodyMedium,
        )

        Space(12.dp)

        OutlinedButton(
            enabled = enabled,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
            onClick = {
                onTrackPickup()
            }
        ) {
            Icon(
                modifier = Modifier.size(18.dp),
                imageVector = vectorResource(Res.drawable.baseline_folder_open_black_24dp),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.tertiary,
            )

            Space(ButtonDefaults.IconSpacing)

            Text(
                text = "Выбор трека",
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}
