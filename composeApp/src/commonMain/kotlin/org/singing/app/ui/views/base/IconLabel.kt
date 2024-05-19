package org.singing.app.ui.views.base

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun IconLabel(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    label: String,
    trailingIcon: ImageVector? = null,
    leadingIcon: ImageVector? = null,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (leadingIcon != null) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = leadingIcon,
                tint = color,
                contentDescription = "",
            )
        }

        Text(
            text = label,
            color = color,
            style = MaterialTheme.typography.labelLarge,
        )

        if (trailingIcon != null) {
            Icon(
                imageVector = trailingIcon,
                tint = color,
                contentDescription = "",
            )
        }
    }
}
