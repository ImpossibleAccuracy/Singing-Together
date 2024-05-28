package com.singing.app.common.views.base

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun AppTextButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String,
    trailingIcon: ImageVector? = null,
    leadingIcon: ImageVector? = null,
    contentColor: Color,
    onClick: () -> Unit,
) {
    TextButton(
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(
            contentColor = contentColor,
        ),
        enabled = enabled,
        onClick = onClick,
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = "",
            )

            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
        }

        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
        )

        if (trailingIcon != null) {
            Spacer(Modifier.width(ButtonDefaults.IconSpacing))

            Icon(
                imageVector = trailingIcon,
                contentDescription = "",
            )
        }
    }
}


@Composable
fun AppFilledButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String,
    trailingIcon: ImageVector? = null,
    leadingIcon: ImageVector? = null,
    containerColor: Color,
    contentColor: Color = contentColorFor(containerColor),
    onClick: () -> Unit,
) {
    FilledTonalButton(
        modifier = modifier,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        enabled = enabled,
        onClick = onClick,
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                tint = contentColor,
                contentDescription = "",
            )

            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
        }

        Text(
            text = label,
            color = contentColor,
            style = MaterialTheme.typography.labelLarge,
        )

        if (trailingIcon != null) {
            Spacer(Modifier.width(ButtonDefaults.IconSpacing))

            Icon(
                imageVector = trailingIcon,
                tint = contentColor,
                contentDescription = "",
            )
        }
    }
}
