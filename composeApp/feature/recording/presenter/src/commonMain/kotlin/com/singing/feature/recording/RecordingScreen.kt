package com.singing.feature.recording

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.currentOrThrow
import com.singing.app.domain.payload.RecordSaveData
import com.singing.app.navigation.AppNavigator
import com.singing.app.navigation.SharedScreen
import com.singing.app.ui.screen.dimens
import com.singing.feature.recording.save.RecordSaveAdditionalInfo
import com.singing.feature.recording.save.RecordSaveDialog
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
    RecordSave(viewModel, uiState)

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

@Composable
private fun RecordSave(
    viewModel: RecordingViewModel,
    uiState: RecordingUiState
) {
    val navigator = AppNavigator.currentOrThrow
    val recordResult by viewModel.recordResult.collectAsState()

    var isDialogVisible by remember { mutableStateOf(false) }

    LaunchedEffect(recordResult) {
        isDialogVisible = recordResult != null
    }

    if (recordResult != null && isDialogVisible) {
        RecordSaveDialog(
            data = RecordSaveAdditionalInfo(
                saveData = RecordSaveData(
                    title = null, // TODO
                    record = recordResult!!.bytes,
                    track = uiState.trackData?.selectedAudio
                ),
                duration = recordResult!!.duration,
                user = uiState.user,
                history = uiState.history,
            ),
            onDismiss = {
                isDialogVisible = false
            },
            onSaved = {
                isDialogVisible = false
                navigator.replace(SharedScreen.RecordList(it))
            },
            onError = {
                isDialogVisible = false
            }
        )
    }
}
