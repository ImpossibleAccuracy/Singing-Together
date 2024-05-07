package org.singing.app.ui.views

import androidx.compose.material.contentColorFor
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import org.singing.app.ui.base.Space

@Composable
fun FilledButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String,
    icon: ImageVector? = null,
    startIcon: ImageVector? = null,
    color: Color,
    contentColor: Color = contentColorFor(color),
    onClick: () -> Unit,
) {
    FilledTonalButton(
        modifier = modifier,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = color,
            contentColor = contentColor,
        ),
        enabled = enabled,
        onClick = onClick,
    ) {
        if (startIcon != null) {
            Icon(
                imageVector = startIcon,
                tint = contentColor,
                contentDescription = "",
            )

            Space(ButtonDefaults.IconSpacing)
        }

        Text(
            text = label,
            color = contentColor,
            style = MaterialTheme.typography.labelLarge,
        )

        if (icon != null) {
            Space(ButtonDefaults.IconSpacing)

            Icon(
                imageVector = icon,
                tint = contentColor,
                contentDescription = "",
            )
        }
    }
}
