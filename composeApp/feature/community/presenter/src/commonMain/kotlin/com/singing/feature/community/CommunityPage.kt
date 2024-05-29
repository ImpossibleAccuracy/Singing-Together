package com.singing.feature.community

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.singing.app.navigation.base.AppPage
import com.singing.app.navigation.base.screenModel
import com.singing.app.navigation.views.ContentContainer
import com.singing.app.navigation.views.DefaultPagePaddings

class CommunityPage : AppPage<CommunityViewModel>() {
    @Composable
    override fun rememberLocalModelFactory(): CommunityViewModel {
        return screenModel<CommunityViewModel>()
    }

    @Composable
    override fun Content(screenModel: CommunityViewModel) {
        ContentContainer {
            val uiState by screenModel.uiState.collectAsState()

            CommunityScreen(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(DefaultPagePaddings),
                viewModel = screenModel,
                uiState = uiState,
            )
        }
    }
}