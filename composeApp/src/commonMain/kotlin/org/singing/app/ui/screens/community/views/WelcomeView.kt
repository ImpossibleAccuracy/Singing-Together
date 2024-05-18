package org.singing.app.ui.screens.community.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.singing.app.ui.base.Space
import org.singing.app.ui.theme.extended


private const val sizeScaleX = 0.76f
private const val sizeScaleY = 0.466f


@Composable
fun WelcomeView(
    modifier: Modifier = Modifier,
    onActionClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(56.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(height = 248.dp)
            .clip(shape = RoundedCornerShape(36.dp))
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
                .clip(shape = GenericShape { size, _ ->
                    val ovalSize = Size(
                        width = size.width / sizeScaleX,
                        height = size.height / sizeScaleY,
                    )

                    val position = Offset(
                        size.width - ovalSize.width,
                        size.height / 2 - ovalSize.height / 2,
                    )

                    addOval(
                        Rect(
                            offset = position,
                            size = ovalSize,
                        )
                    )
                })
                .background(color = MaterialTheme.extended.communityBannerColor)
                .padding(
                    start = 64.dp,
                    end = 86.dp
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "A space to grow,\nlearn, & share",
                color = MaterialTheme.extended.onCommunityBannerColor,
                style = TextStyle(
                    fontSize = 45.sp,
                    fontWeight = FontWeight.Bold,
                )
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    end = 64.dp,
                    top = 36.dp,
                    bottom = 36.dp
                ),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                text = "Join our community and share your voice with other users and hear them sing back.",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.End,
            )

            Space(36.dp)

            FilledTonalButton(
                modifier = Modifier.widthIn(min = 200.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.extended.communityBannerColor,
                    contentColor = MaterialTheme.extended.onCommunityBannerColor,
                ),
                onClick = onActionClick,
            ) {
                Text(
                    text = "Let’s start",
                    color = MaterialTheme.extended.onCommunityBannerColor,
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                    )
                )
            }
        }
    }
}
