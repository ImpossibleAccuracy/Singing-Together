package com.singing.app.common.views.base.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp


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
            .height(36.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showAvatar && avatar != null && avatarAtStart) {
            UserAvatar(avatar = avatar)
        }

        Text(
            modifier = Modifier.padding(horizontal = 12.dp),
            text = username,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelLarge,
        )

        if (showAvatar && avatar != null && !avatarAtStart) {
            UserAvatar(avatar = avatar)
        }
    }
}

@Composable
private fun AccountChipAvatar(avatar: () -> Painter) {
    Image(
        modifier = Modifier
            .size(size = 36.dp)
            .clip(shape = RoundedCornerShape(50)),
        painter = avatar(),
        contentScale = ContentScale.Crop,
        contentDescription = "Avatar",
    )
}

