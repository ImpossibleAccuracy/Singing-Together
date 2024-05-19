package org.singing.app.ui.views.base.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.baseline_person_24
import org.jetbrains.compose.resources.painterResource
import org.singing.app.ui.base.cardAppearance

private const val avatarSize = 36
private const val largeAvatarSize = 48
private const val horizontalPadding = 16

@Composable
fun AccountChip(
    modifier: Modifier = Modifier,
    username: String,
    isAvatarLarge: Boolean = true,
    avatar: () -> (Painter?),
    showAvatar: Boolean = true,
) {
    Box(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .height(height = avatarSize.dp)
                .cardAppearance(
                    shape = RoundedCornerShape(50),
                    background = MaterialTheme.colorScheme.surface,
                    padding = PaddingValues(
                        start = horizontalPadding.dp,
                        end = (avatarSize + horizontalPadding).dp,
                    )
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = username,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelLarge,
            )
        }

        if (showAvatar) {
            Image(
                painter = avatar()
                    ?: painterResource(Res.drawable.baseline_person_24),
                contentScale = ContentScale.Crop,
                contentDescription = "Avatar",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(
                        size = when (isAvatarLarge) {
                            true -> largeAvatarSize.dp
                            false -> avatarSize.dp
                        }
                    )
                    .clip(shape = RoundedCornerShape(50))
                    .background(color = MaterialTheme.colorScheme.surface)
            )
        }
    }
}
