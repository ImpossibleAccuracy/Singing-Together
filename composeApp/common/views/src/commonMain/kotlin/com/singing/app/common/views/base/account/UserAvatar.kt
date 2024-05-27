package com.singing.app.common.views.base.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import com.singing.app.common.views.views.generated.resources.Res
import com.singing.app.common.views.views.generated.resources.baseline_person_24
import org.jetbrains.compose.resources.vectorResource

@Composable
fun UserAvatar(
    modifier: Modifier = Modifier
        .size(size = 36.dp)
        .clip(shape = RoundedCornerShape(50)),
    avatar: String?
) {
    Box(modifier = modifier) {
        if (avatar == null) {
            Icon(
                modifier = Modifier.size(size = 36.dp),
                imageVector = vectorResource(Res.drawable.baseline_person_24),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = "Avatar",
            )
        } else {
            val painter = rememberImagePainter(avatar)

            Image(
                modifier = Modifier,
                painter = painter,
                contentScale = ContentScale.Crop,
                contentDescription = "Avatar",
            )
        }
    }
}
