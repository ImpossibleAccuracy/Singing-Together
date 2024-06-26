package com.singing.feature.account.profile.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.singing.app.common.views.base.AppTextButton
import com.singing.app.common.views.base.account.UserAvatar
import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.UserData
import com.singing.app.domain.model.UserInfo
import com.singing.app.ui.screen.dimens
import com.singing.feature.account.profile.presenter.generated.resources.*
import kotlinx.datetime.Clock
import nl.jacobras.humanreadable.HumanReadable
import org.jetbrains.compose.resources.stringResource


@Composable
fun AccountBanner(
    modifier: Modifier = Modifier,
    currentUser: UserData?,
    account: UserData,
    userInfo: DataState<UserInfo>,
    onLogout: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        UserAvatar(
            size = MaterialTheme.dimens.dimen5_5 * 3,
            avatar = account.avatar,
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1)
        ) {
            Text(
                text = account.username,
                color = MaterialTheme.colorScheme.onBackground,
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            )

            when (userInfo) {
                DataState.Empty, is DataState.Error -> {
                    Text(
                        text = stringResource(Res.string.title_cannot_fetch_user_info),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                DataState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                is DataState.Success -> {
                    AccountInfoSection(userInfo.data)
                }
            }
        }

        if (currentUser == account) {
            AppTextButton(
                label = stringResource(Res.string.action_logout),
                onClick = onLogout,
                contentColor = MaterialTheme.colorScheme.primary,
            )
        }
    }
}


@Composable
private fun AccountInfoSection(userInfo: UserInfo) {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen0_5)) {
        Text(
            text = stringResource(
                resource = Res.string.label_publications_count,
                userInfo.publicationsCount
            ),
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = 1.43.em,
            style = MaterialTheme.typography.labelLarge
        )

        Text(
            text = stringResource(
                resource = Res.string.label_account_registered_since,
                HumanReadable.duration(Clock.System.now() - userInfo.registeredAt.instant)
            ),
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = 1.43.em,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
