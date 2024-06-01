package com.singing.app.common.views.base

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.singing.app.ui.screen.dimens
import com.singing.app.ui.screen.icon

@Composable
fun IconLabel(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    label: String,
    trailingIcon: ImageVector? = null,
    leadingIcon: ImageVector? = null,
) {
    IconLabel(
        modifier = modifier,
        color = color,
        label = label,
        trailing = trailingIcon?.let {
            {
                Icon(
                    modifier = Modifier.size(MaterialTheme.dimens.icon),
                    imageVector = it,
                    tint = color,
                    contentDescription = "",
                )
            }
        },
        leading = leadingIcon?.let {
            {
                Icon(
                    modifier = Modifier.size(MaterialTheme.dimens.icon),
                    imageVector = it,
                    tint = color,
                    contentDescription = "",
                )
            }
        },
    )
}

@Composable
fun IconLabel(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    label: String,
    trailing: (@Composable () -> Unit)? = null,
    leading: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1),
    ) {
        leading?.invoke()

        Text(
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            text = label,
            color = color,
            style = MaterialTheme.typography.labelLarge,
        )

        trailing?.invoke()
    }
}
