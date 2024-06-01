package com.singing.feature.recording.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.currentOrThrow
import com.singing.app.common.views.base.list.Loader
import com.singing.app.navigation.AppNavigator
import com.singing.app.navigation.SharedScreen
import com.singing.app.ui.screen.dimens
import com.singing.feature.recording.setup.presenter.generated.resources.Res
import com.singing.feature.recording.setup.presenter.generated.resources.subtitle_select_track
import com.singing.feature.recording.setup.presenter.generated.resources.title_select_track
import com.singing.feature.recording.setup.viewmodel.SelectAudioIntent
import com.singing.feature.recording.setup.viewmodel.SelectAudioUiState
import com.singing.feature.recording.setup.views.AudioChooser
import com.singing.feature.recording.setup.views.SelectedAudioInfo
import org.jetbrains.compose.resources.stringResource


@Composable
fun SelectAudioScreen(
    modifier: Modifier = Modifier,
    viewModel: SelectAudioViewModel,
    uiState: SelectAudioUiState,
) {
    val navigator = AppNavigator.currentOrThrow

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .widthIn(min = 400.dp, max = 750.dp)
                .clip(shape = MaterialTheme.shapes.medium)
                .background(color = MaterialTheme.colorScheme.background)
                .padding(MaterialTheme.dimens.dimen3),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen3),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen0_5),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(Res.string.title_select_track),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.displayMedium,
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = stringResource(Res.string.subtitle_select_track),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )
            }

            if (uiState.isParsing) {
                Loader(Modifier.fillMaxWidth())
            } else if (uiState.trackData == null) {
                AudioChooser(
                    recentTracks = uiState.tracks,
                    onFileSelected = {
                        viewModel.onIntent(SelectAudioIntent.ProcessAudio(it))
                    }
                )
            } else {
                SelectedAudioInfo(
                    audioProcessState = uiState.trackData,
                    onResetState = {
                        viewModel.onIntent(SelectAudioIntent.ClearTrackData)
                    },
                    navigateNext = {
                        navigator.replace(SharedScreen.Recording(uiState.trackData, true))
                    }
                )
            }
        }
    }
}
