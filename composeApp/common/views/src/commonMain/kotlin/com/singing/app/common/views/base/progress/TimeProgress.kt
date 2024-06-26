package com.singing.app.common.views.base.progress

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.singing.app.ui.formatTimeString
import com.singing.app.ui.screen.dimens
import kotlin.math.roundToLong

@Composable
fun TimeProgress(
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    inactiveTrackColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    editable: Boolean,
    totalDuration: Long,
    currentPosition: Long,
    onPositionChange: (Long) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen0_5),
    ) {
        Text(
            text = formatTimeString(currentPosition),
            style = MaterialTheme.typography.labelMedium,
            color = contentColor,
        )

        Slider(
            enabled = editable,
            modifier = Modifier.weight(1f),
            value = (currentPosition.toFloat() / totalDuration),
            colors = SliderDefaults.colors(
                thumbColor = contentColor,
                activeTrackColor = contentColor,
                inactiveTrackColor = inactiveTrackColor,
            ),
            onValueChange = {
                onPositionChange((it * totalDuration).roundToLong())
            },
        )

        Text(
            text = formatTimeString(totalDuration),
            style = MaterialTheme.typography.labelMedium,
            color = contentColor,
        )
    }
}
