package com.singing.app.common.views.base.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.singing.app.ui.screen.dimens


@Composable
fun accountChipAppearance(
    color: Color = MaterialTheme.colorScheme.surface,
) = Modifier
    .clip(shape = RoundedCornerShape(50))
    .background(color)

@Composable
fun AccountChip(
    modifier: Modifier = accountChipAppearance(),
    username: String,
    avatarAtStart: Boolean = false,
    showAvatar: Boolean = true,
    avatar: String? = null,
) {
    Row(
        modifier = modifier then Modifier
            .height(MaterialTheme.dimens.dimen4_5),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showAvatar && avatar != null && avatarAtStart) {
            UserAvatar(avatar = avatar)
        }

        Text(
            modifier = Modifier.padding(horizontal = MaterialTheme.dimens.dimen1_5),
            text = username,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelLarge,
        )

        if (showAvatar && avatar != null && !avatarAtStart) {
            UserAvatar(avatar = avatar)
        }
    }
}

