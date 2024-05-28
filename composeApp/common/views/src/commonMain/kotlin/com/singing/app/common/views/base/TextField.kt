package com.singing.app.common.views.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.dokar.chiptextfield.Chip
import com.dokar.chiptextfield.ChipTextFieldState
import com.dokar.chiptextfield.m3.ChipTextFieldDefaults
import com.dokar.chiptextfield.m3.OutlinedChipTextField
import com.singing.app.common.views.views.generated.resources.Res
import com.singing.app.common.views.views.generated.resources.baseline_close_black_24dp
import com.singing.app.ui.screen.dimens
import com.singing.app.ui.screen.icon
import org.jetbrains.compose.resources.vectorResource


@Composable
fun AppChipTextField(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    readOnlyChips: Boolean = true,
    label: String? = null,
    value: String,
    state: ChipTextFieldState<Chip>,
    onValueChange: (String) -> Unit,
    onSubmit: (String) -> Chip?,
    onRemove: (Chip) -> Unit,
) {
    OutlinedChipTextField(
        modifier = modifier,
        state = state,
        value = value,
        onValueChange = onValueChange,
        onSubmit = onSubmit,
        readOnlyChips = readOnlyChips,
        shape = shape,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = borderColor,
        ),
        chipStyle = ChipTextFieldDefaults.chipStyle(
            shape = MaterialTheme.shapes.small,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        ),
        label = label?.let {
            {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        },
        chipTrailingIcon = {
            Box(
                modifier = Modifier
                    .padding(
                        top = MaterialTheme.dimens.dimen0_25,
                        end = MaterialTheme.dimens.dimen0_5,
                    )
                    .size(MaterialTheme.dimens.icon)
                    .clip(RoundedCornerShape(50))
                    .clickable {
                        onRemove(it)
                    }
                    .padding(MaterialTheme.dimens.dimen0_5)
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = vectorResource(Res.drawable.baseline_close_black_24dp),
                    contentDescription = "",
                )
            }
        },
    )
}
