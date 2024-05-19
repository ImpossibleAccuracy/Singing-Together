package org.singing.app.ui.screens.record.create

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.singing.app.di.module.viewModels
import org.singing.app.setup.collectAsStateSafe
import org.singing.app.ui.base.Space
import org.singing.app.ui.common.ContentContainer
import org.singing.app.ui.common.DefaultPagePaddings
import org.singing.app.ui.screens.record.create.save.RecordSaveDialog
import org.singing.app.ui.screens.record.create.viewmodel.RecordingViewModel
import org.singing.app.ui.screens.record.create.viewmodel.state.AudioProcessState
import org.singing.app.ui.screens.record.create.views.Display
import org.singing.app.ui.screens.record.create.views.DisplayInfo
import org.singing.app.ui.screens.record.create.views.RecordHistory
import org.singing.app.ui.screens.record.list.RecordListScreen
import org.singing.app.ui.views.shared.player.PlayerView

data class RecordingScreen(
    val audio: AudioProcessState? = null,
    val isNewInstance: Boolean = true,
) : Screen {
    @Composable
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        state = verticalScrollState
                    )
                    .padding(DefaultPagePaddings),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                DisplayContainer(
                    viewModel = viewModel,
                )

                Space(24.dp)

                AudioPlayerContainer(
                    viewModel = viewModel,
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = MaterialTheme.shapes.medium)
                ) {
                    DisplayInfoContainer(
                        viewModel = viewModel,
                    )

                    RecordHistoryContainer(
                        viewModel = viewModel,
                    )
                }
            }
        }
    }

    @Composable
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
    }


    @Composable
    private fun DisplayContainer(
        viewModel: RecordingViewModel,
    ) {
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
    private fun AudioPlayerContainer(
        viewModel: RecordingViewModel,
    ) {
        val uiState by viewModel.uiState.collectAsStateSafe()
        val audioProcessState = uiState.audioProcessState

        if (audioProcessState != null) {
            PlayerView(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerLowest,
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

            Spacer(Modifier.height(24.dp))
        }
    }

    @Composable
    private fun DisplayInfoContainer(
        viewModel: RecordingViewModel,
    ) {
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
    private fun RecordHistoryContainer(
        viewModel: RecordingViewModel,
    ) {
        val uiState by viewModel.uiState.collectAsStateSafe()

        RecordHistory(
            history = uiState.history,
            note = viewModel::getNote
        )
    }
}
