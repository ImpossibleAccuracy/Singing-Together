package org.singing.app.ui.screens.main.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.*
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.main_record_banner
import com.singing.app.composeapp.generated.resources.quest
import com.singing.app.composeapp.generated.resources.title_user_welcome
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.singing.app.domain.model.AccountUiData
import org.singing.app.ui.base.Space
import org.singing.app.ui.theme.extended
import org.singing.app.ui.views.base.AppFilledButton


@Composable
fun RecordBanner(
    user: AccountUiData?,
    onAction: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(256.dp)
            .clip(shape = RoundedCornerShape(36.dp))
            .paint(
                painter = painterResource(Res.drawable.main_record_banner),
                contentScale = ContentScale.Crop,
            )
            .background(
                color = MaterialTheme.extended.mainBannerScrim,
            ),
        verticalArrangement = Arrangement.Center,
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

        Space(24.dp)

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