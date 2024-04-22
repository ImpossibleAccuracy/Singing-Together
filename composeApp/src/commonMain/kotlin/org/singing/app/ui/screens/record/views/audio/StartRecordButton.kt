package org.singing.app.ui.screens.record.views.audio

import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.baseline_play_arrow_black_24dp
import com.singing.app.composeapp.generated.resources.baseline_radio_button_checked_black_24dp
import com.singing.app.composeapp.generated.resources.baseline_stop_black_24dp
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.ui.helper.Space

@Composable
fun StartRecordButton(
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    FilledTonalButton(
        onClick = onClick,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.inversePrimary,
        ),
    ) {
        val text = if (isPlaying) "Остановить" else "Запись"
        val icon =
            if (isPlaying) Res.drawable.baseline_stop_black_24dp else Res.drawable.baseline_radio_button_checked_black_24dp

        Icon(
            modifier = Modifier
                .size(18.dp),
            imageVector = vectorResource(icon),
            contentDescription = "radiobox-marked",
            tint = MaterialTheme.colorScheme.primary,
        )

        Space(8.dp)

        Text(
            text = text,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}
