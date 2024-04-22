package org.singing.app.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.singing.app.ComposeView
import org.singing.app.domain.model.ListWithSelected
import org.singing.app.ui.helper.Space


@Immutable
data class DropdownSelectChipData<T>(
    val default: String? = null,
    val items: ListWithSelected<T>,
)

@Immutable
data class DropdownSelectChipActions<T>(
    val onItemSelected: ((value: T?) -> Unit)? = null,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropdownSelectMenu(
    modifier: Modifier,
    data: DropdownSelectChipData<T>,
    actions: DropdownSelectChipActions<T>,
    itemLabelText: (item: T) -> String = { it.toString() },
    icon: ComposeView? = null,
) {
    val labelText = data.items.selectedItem?.let { itemLabelText(it) }
        ?: data.default!!

    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = {
            isExpanded = false
        }
    ) {
        Surface(
            modifier = modifier.menuAnchor(),
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.primaryContainer,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .heightIn(min = 32.dp)
                    .clickable {
                        isExpanded = !isExpanded
                    }
                    .padding(16.dp, 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                icon?.invoke()

                Space(8.dp)

                Text(
                    modifier = Modifier.weight(1f),
                    text = labelText,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                )

                Space(8.dp)

                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }

        ExposedDropdownMenu(
            modifier = Modifier.exposedDropdownSize(),
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            }
        ) {
            if (data.default != null) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = data.default,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    },
                    onClick = {
                        actions.onItemSelected?.invoke(null)

                        isExpanded = false
                    }
                )
            }

            data.items.list.forEach {
                val itemLabel = itemLabelText(it)

                DropdownMenuItem(
                    text = {
                        Text(
                            text = itemLabel,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    },
                    onClick = {
                        actions.onItemSelected?.invoke(it)

                        isExpanded = false
                    }
                )
            }
        }
    }
}
