package com.singing.feature.account.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.singing.app.domain.model.UserData
import com.singing.app.navigation.base.AppPage
import com.singing.app.navigation.base.screenModel
import com.singing.app.navigation.views.ContentContainer
import com.singing.app.navigation.views.DefaultPagePaddings

data class AccountProfilePage(
    val account: UserData,
) : AppPage<AccountProfileViewModel>() {
    @Composable
    override fun rememberLocalModelFactory(): AccountProfileViewModel {
        return screenModel<AccountProfileViewModel>(account)
    }

    @Composable
    override fun Content(screenModel: AccountProfileViewModel) {
        ContentContainer {
            val uiState by screenModel.uiState.collectAsState()

            AccountProfileScreen(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = DefaultPagePaddings,
                viewModel = screenModel,
                uiState = uiState,
            )
        }
    }
}
