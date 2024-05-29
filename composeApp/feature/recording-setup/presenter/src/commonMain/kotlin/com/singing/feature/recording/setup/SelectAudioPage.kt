package com.singing.feature.recording.setup

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.singing.app.navigation.base.AppPage
import com.singing.app.navigation.base.screenModel
import com.singing.app.navigation.views.ContentContainer

class SelectAudioPage : AppPage<SelectAudioViewModel>() {
    @Composable
    override fun rememberLocalModelFactory(): SelectAudioViewModel {
        return screenModel<SelectAudioViewModel>()
    }

    @Composable
    override fun Content(screenModel: SelectAudioViewModel) {
        ContentContainer {
            val uiState by screenModel.uiState.collectAsState()

            SelectAudioScreen(
                modifier = Modifier.fillMaxWidth(),
                viewModel = screenModel,
                uiState = uiState,
            )
        }
    }
}
