package com.singing.feature.recording

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.singing.app.domain.model.TrackParseResult
import com.singing.app.navigation.base.AppPage
import com.singing.app.navigation.base.screenModel
import com.singing.app.navigation.views.ContentContainer
import com.singing.app.navigation.views.DefaultPagePaddings
import com.singing.feature.recording.viewmodel.RecordingIntent

data class RecordingPage(
    val audio: TrackParseResult? = null,
    val isNewInstance: Boolean = true,
) : AppPage<RecordingViewModel>() {
    @Composable
    override fun rememberLocalModelFactory(): RecordingViewModel {
        return screenModel()
    }

    @Composable
    override fun Content(screenModel: RecordingViewModel) {
        LaunchedEffect(audio, isNewInstance) {
            if (isNewInstance) {
                screenModel.onIntent(
                    if (audio == null) RecordingIntent.ClearTrack
                    else RecordingIntent.UpdateTrack(audio)
                )
            }
        }

        ContentContainer {
            val uiState by screenModel.uiState.collectAsState()

            RecordingScreen(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(DefaultPagePaddings),
                viewModel = screenModel,
                uiState = uiState,
            )
        }
    }
}
