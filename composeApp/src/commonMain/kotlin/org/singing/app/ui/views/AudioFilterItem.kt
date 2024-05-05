package org.singing.app.ui.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.onClick
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.baseline_close_black_24dp
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AudioFilterItem(
    modifier: Modifier = Modifier,
    title: String,
    closeable: Boolean = true,
    onClose: (() -> Unit)? = null,
) {
    InputChip(
        modifier = modifier
            .fillMaxWidth()
            .requiredHeight(32.dp),
        shape = MaterialTheme.shapes.extraSmall,
        border = null,
        colors = InputChipDefaults.inputChipColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        leadingIcon = {
//            if (draggable) {
//                Icon(
//                    imageVector = vectorResource(Res.drawable.baseline_drag_handle_black_24dp),
//                    contentDescription = "drag-horizontal-variant",
//                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
//                )
//            }
        },
        label = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelMedium,
            )
        },
        trailingIcon = {
            if (closeable) {
                Icon(
                    modifier = Modifier.onClick {
                        onClose?.invoke()
                    },
                    imageVector = vectorResource(Res.drawable.baseline_close_black_24dp),
                    contentDescription = "close",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        selected = false,
        onClick = {}
    )
}
