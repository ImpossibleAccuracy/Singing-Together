package com.singing.feature.auth

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.singing.app.navigation.base.AppPage
import com.singing.app.navigation.base.screenModel
import com.singing.app.navigation.views.DefaultPagePaddings

class AuthPage : AppPage<AuthViewModel>() {
    @Composable
    override fun rememberLocalModelFactory(): AuthViewModel {
        return screenModel()
    }

    @Composable
    override fun Content(screenModel: AuthViewModel) {
        val uiState by screenModel.uiState.collectAsState()

        AuthScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(DefaultPagePaddings),
            viewModel = screenModel,
            uiState = uiState,
        )
    }
}