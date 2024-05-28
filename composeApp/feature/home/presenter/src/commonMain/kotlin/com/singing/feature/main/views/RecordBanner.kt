package com.singing.feature.main.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import com.singing.app.common.views.base.AppFilledButton
import com.singing.app.domain.model.UserData
import com.singing.app.ui.screen.dimens
import com.singing.app.ui.theme.extended.extended
import com.singing.feature.main.presenter.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun RecordBanner(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(MaterialTheme.dimens.dimen4_5),
    user: UserData?,
    onAction: () -> Unit,
) {
    Column(
        modifier = modifier
            .clip(shape = shape)
            .paint(
                painter = painterResource(Res.drawable.main_record_banner),
                contentScale = ContentScale.Crop,
            )
            .background(
                color = MaterialTheme.extended.mainBannerScrim,
            ),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen3, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(
                resource = Res.string.title_user_welcome,
                user?.username ?: stringResource(Res.string.quest),
            ),
            color = MaterialTheme.extended.onMainBannerScrim,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
        )

        AppFilledButton(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            label = stringResource(Res.string.action_start_now),
            onClick = {
                onAction()
            }
        )
    }
}