package com.singing.feature.recording

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.singing.app.ui.screen.dimens
import com.singing.feature.recording.viewmodel.RecordingIntent
import com.singing.feature.recording.viewmodel.RecordingUiState
import com.singing.feature.recording.views.AudioDisplay
import com.singing.feature.recording.views.AudioPlayerContainer
import com.singing.feature.recording.views.RecordHistory
import com.singing.feature.recording.views.RecorderControls


@Composable
fun RecordingScreen(
    modifier: Modifier,
    viewModel: RecordingViewModel,
    uiState: RecordingUiState,
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen2_5),
    ) {
        val input by viewModel.audioInput.collectAsState()

        AudioDisplay(
            modifier = Modifier
                .fillMaxWidth()
                .height(256.dp)
                .clip(shape = RoundedCornerShape(36.dp)),
            input = input,
            recordCountdown = when (uiState) {
                is RecordingUiState.Countdown -> uiState.recordCountdown
                else -> null
            },
            note = viewModel::getNote,
            stopRecordCountdown = {
                viewModel.onIntent(RecordingIntent.StopRecordCountdown)
            }
        )

        if (uiState.trackData != null) {
            AudioPlayerContainer(
                modifier = Modifier.fillMaxWidth(),
                uiState = uiState,
                newIntent = viewModel::onIntent,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.medium)
        ) {
            val recordDuration by viewModel.recordDuration.collectAsState()

            RecorderControls(
                isRecording = uiState is RecordingUiState.Recording,
                canRecord = uiState !is RecordingUiState.Countdown,
                recordDuration = recordDuration,
                onRecordStart = {
                    viewModel.onIntent(RecordingIntent.StartRecording)
                },
                onRecordFinish = {
                    viewModel.onIntent(RecordingIntent.StopRecording)
                }
            )

            RecordHistory(
                history = uiState.history,
                note = viewModel::getNote,
            )
        }
    }
}


/*@Composable
    override fun Content() {
        val viewModel = viewModels<RecordingViewModel>(true)

        LifecycleEffect {
            viewModel.stopRecordCountdown()

            if (!viewModel.isAnyActionActive) {
                viewModel.stopActionsAndClearData()
            }
        }

        val verticalScrollState = rememberScrollState()

        LaunchedEffect(audio) {
            if (isNewInstance) {
                if (audio == null) {
                    viewModel.stopActionsAndClearData()
                } else {
                    viewModel.setProcessedAudio(audio)
                }
            }
        }

        RecordSaveDialogContainer(viewModel)

        ContentContainer {
        }
    }*/

/*@Composable
private fun RecordSaveDialogContainer(
    viewModel: RecordingViewModel,
) {
    val navigator = LocalNavigator.currentOrThrow

    val uiState by viewModel.uiState.collectAsStateSafe()

    var isSaveDialogVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.recordSaveData) {
        isSaveDialogVisible = (uiState.recordSaveData != null)
    }

    if (isSaveDialogVisible) {
        RecordSaveDialog(
            data = uiState.recordSaveData!!,
            navigateToRecord = {
                navigator.push(
                    RecordListScreen(
                        defaultSelectedRecord = it,
                    )
                )
            },
            onDismiss = {
                isSaveDialogVisible = false
            },
        )
    }
}*/
