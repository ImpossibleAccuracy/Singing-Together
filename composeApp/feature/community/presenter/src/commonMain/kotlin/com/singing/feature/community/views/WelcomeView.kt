package com.singing.feature.community.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
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
import com.singing.app.ui.screen.dimens
import com.singing.app.ui.theme.extended.extended
import com.singing.app.ui.utils.cardAppearance
import com.singing.feature.community.presenter.generated.resources.Res
import com.singing.feature.community.presenter.generated.resources.action_lets_start
import com.singing.feature.community.presenter.generated.resources.subtitle_community_banner
import com.singing.feature.community.presenter.generated.resources.title_community_banner
import org.jetbrains.compose.resources.stringResource


private const val sizeScaleX = 0.76f
private const val sizeScaleY = 0.466f


@Composable
@NonRestartableComposable
fun WelcomeView(
    modifier: Modifier = Modifier,
    onActionClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(height = 276.dp)
            .clip(shape = RoundedCornerShape(MaterialTheme.dimens.dimen4_5))
            .background(color = MaterialTheme.colorScheme.primary),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen3, Alignment.Start),
    ) {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
                .cardAppearance(
                    shape = GenericShape { size, _ ->
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
                    },
                    background = MaterialTheme.extended.communityBannerColor,
                    padding = PaddingValues(
                        start = MaterialTheme.dimens.dimen4 * 2,
                        end = MaterialTheme.dimens.dimen4 * 2,
                    )
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(Res.string.title_community_banner),
                color = MaterialTheme.extended.onCommunityBannerColor,
                style = TextStyle(
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                )
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    end = MaterialTheme.dimens.dimen4 * 2,
                    top = MaterialTheme.dimens.dimen3,
                    bottom = MaterialTheme.dimens.dimen3
                ),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen3, Alignment.Bottom),
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                text = stringResource(Res.string.subtitle_community_banner),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.End,
            )

            FilledTonalButton(
                modifier = Modifier
                    .widthIn(min = 200.dp)
                    .requiredHeight(52.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.extended.communityBannerColor,
                    contentColor = MaterialTheme.extended.onCommunityBannerColor,
                ),
                onClick = onActionClick,
            ) {
                Text(
                    text = stringResource(Res.string.action_lets_start),
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
