package com.singing.app.common.views.base.list

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.singing.app.ui.screen.dimens

@Composable
fun EmptyView(
    modifier: Modifier = Modifier.fillMaxSize(),
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    icon: (@Composable () -> Unit)? = null,
    title: String,
    subtitle: String,
    action: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        icon?.invoke()

        Text(
            text = title,
            color = contentColor,
            style = MaterialTheme.typography.titleLarge,
        )

        Text(
            text = subtitle,
            color = contentColor,
            style = MaterialTheme.typography.bodyMedium,
        )

        action?.let {
            Spacer(Modifier.height(MaterialTheme.dimens.dimen1))

            it()
        }
    }
}
