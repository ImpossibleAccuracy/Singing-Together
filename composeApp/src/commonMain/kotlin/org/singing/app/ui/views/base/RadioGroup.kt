package org.singing.app.ui.views.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList

@Composable
fun <T> RadioGroup(
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.primary,
    items: ImmutableList<T>,
    selectedItem: T,
    onSelectedItemChanged: (T) -> Unit,
    divider: (@Composable () -> Unit)? = null,
    content: @Composable (T) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        items.forEachIndexed { index, item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSelectedItemChanged(item)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Box(Modifier.weight(1f)) {
                    content(item)
                }

                RadioButton(
                    selected = selectedItem == item,
                    onClick = {
                        onSelectedItemChanged(item)
                    },
                    enabled = true,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = contentColor,
                    )
                )
            }

            if (index != items.lastIndex) {
                divider?.invoke()
            }
        }
    }
}
