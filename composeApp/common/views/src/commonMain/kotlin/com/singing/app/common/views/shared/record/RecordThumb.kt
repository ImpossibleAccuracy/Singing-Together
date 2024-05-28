package com.singing.app.common.views.shared.record

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.singing.app.ui.screen.dimens


@Composable
fun RecordThumb(
    color: Color = MaterialTheme.colorScheme.surface,
    size: Dp = MaterialTheme.dimens.dimen4 * 4,
    textStyle: TextStyle = MaterialTheme.typography.titleLarge,
    accuracy: Int?,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(size = size)
            .clip(shape = MaterialTheme.shapes.medium)
            .background(color = color)
    ) {
        Text(
            text = when (accuracy) {
                null -> "N/A"
                else -> "$accuracy%"
            },
            color = MaterialTheme.colorScheme.onSurface,
            style = textStyle
        )
    }
}
