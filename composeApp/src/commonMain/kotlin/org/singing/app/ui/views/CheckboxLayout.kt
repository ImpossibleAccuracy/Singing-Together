package org.singing.app.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.singing.app.ComposeView
import org.singing.app.ui.helper.Space

@Composable
fun CheckboxLayout(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .clip(MaterialTheme.shapes.small)
        .background(MaterialTheme.colorScheme.surfaceContainer)
        .padding(
            start = 16.dp,
            top = 0.dp,
            bottom = 0.dp,
            end = 4.dp,
        ),
    enabled: Boolean = true,
    checked: Boolean,
    onChanged: ((Boolean) -> Unit)? = null,
    label: ComposeView,
    icon: ComposeView? = null,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon?.invoke()

        Space(12.dp)

        label.invoke()

        Spacer(
            modifier = Modifier.weight(1f),
        )

        Checkbox(
            enabled = enabled,
            checked = checked,
            onCheckedChange = {
                onChanged?.invoke(it)
            }
        )
    }
}
