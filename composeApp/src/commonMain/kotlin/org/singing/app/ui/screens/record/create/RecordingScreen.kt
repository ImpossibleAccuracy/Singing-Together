package org.singing.app.ui.screens.record.create

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.singing.app.di.module.viewModels
import org.singing.app.setup.collectAsStateSafe
import org.singing.app.ui.base.AppScreen
import org.singing.app.ui.base.Space
import org.singing.app.ui.screens.record.create.viewmodel.RecordingViewModel
import org.singing.app.ui.screens.record.create.viewmodel.state.AudioProcessState
import org.singing.app.ui.screens.record.create.views.Display
import org.singing.app.ui.screens.record.create.views.DisplayInfo
import org.singing.app.ui.screens.record.create.views.RecordHistory
import org.singing.app.ui.views.shared.player.PlayerView

class RecordingScreen(
    private val audio: AudioProcessState? = null,
    private var isNewInstance: Boolean = true,
) : AppScreen() {
    private var _viewModel: RecordingViewModel? = null
    private val viewModel get() = _viewModel!!

    override fun onClose() {
        super.onClose()

        viewModel.stopRecordCountdown()

        if (!viewModel.isAnyActionActive) {
            viewModel.stopActionsAndClearData()
        }
    }

    @Composable
    override fun Content() {
        _viewModel = viewModels(true)

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

        ContentContainer {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        state = verticalScrollState
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 1000.dp)
                        .fillMaxWidth()
                        .padding(
                            vertical = 24.dp,
                        )
                ) {
                    DisplayContainer()

                    Space(24.dp)

                    AudioPlayerContainer().let { isVisible ->
                        if (isVisible) {
                            Space(24.dp)
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = MaterialTheme.shapes.medium)
                    ) {
                        DisplayInfoContainer()

                        RecordHistoryContainer()
                    }
                }
            }
        }
    }


    @Composable
    private fun DisplayContainer() {
        val recordData by viewModel.audioInputData.collectAsStateSafe()
        val recordCountdown by viewModel
            .uiState
            .map { it.recordCountdown }
            .distinctUntilChanged()
            .collectAsStateSafe(null)

        Display(
            firstInput = recordData.firstInput,
            secondInput = recordData.secondInput,
            recordCountdown = recordCountdown,
        )
    }

    @Composable
    private fun AudioPlayerContainer(): Boolean {
        val uiState by viewModel.uiState.collectAsStateSafe()
        val audioProcessState = uiState.audioProcessState

        if (audioProcessState != null) {
            PlayerView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        top = 18.dp,
                        end = 16.dp,
                        bottom = 12.dp,
                    ),
                editable = uiState.canEditPlayerState,
                totalDuration = audioProcessState.selectedAudio.duration,
                currentPosition = uiState.playerPosition,
                isPlaying = uiState.isPlaying,
                onPositionChange = {
                    viewModel.setPlayerPosition(it)
                },
                onPlay = {
                    viewModel.startPlaying()
                },
                onStop = {
                    viewModel.stopPlaying()
                }
            )
        }

        return audioProcessState != null
    }

    @Composable
    private fun DisplayInfoContainer() {
        val uiState by viewModel.uiState.collectAsStateSafe()

        // TODO: recomposition bugs
        val recordDuration by viewModel.recordDuration.collectAsStateSafe()

        DisplayInfo(
            isRecording = uiState.isRecording,
            canRecord = uiState.canEditPlayerState,
            recordDuration = recordDuration,
            onRecordStart = viewModel::startRecord,
            onRecordFinish = viewModel::stopRecord,
        )
    }

    @Composable
    private fun RecordHistoryContainer() {
        val history by viewModel.uiState
            .map { it.history }
            .collectAsStateSafe(listOf())

        RecordHistory(
            history = history,
            note = viewModel::getNote
        )
    }
}
