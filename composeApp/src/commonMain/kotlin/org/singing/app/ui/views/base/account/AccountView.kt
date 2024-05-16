package org.singing.app.ui.views.base.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.baseline_person_24
import org.jetbrains.compose.resources.painterResource

private const val avatarSize = 48
private const val horizontalPadding = 16

@Composable
fun AccountView(
    modifier: Modifier = Modifier,
    username: String,
    avatar: Painter? = null,
    showAvatar: Boolean = true,
) {
    Box(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .height(height = 36.dp)
                .clip(
                    shape = RoundedCornerShape(50)
                )
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(
                    start = horizontalPadding.dp,
                    end = (avatarSize + horizontalPadding).dp,
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
                painter = avatar ?: painterResource(Res.drawable.baseline_person_24),
                contentScale = ContentScale.Crop,
                contentDescription = "Avatar",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(size = avatarSize.dp)
                    .clip(shape = RoundedCornerShape(50))
                    .background(color = MaterialTheme.colorScheme.surface)
            )
        }
    }
}
