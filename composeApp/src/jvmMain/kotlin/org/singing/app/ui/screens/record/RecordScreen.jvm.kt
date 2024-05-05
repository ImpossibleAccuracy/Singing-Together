package org.singing.app.ui.screens.record

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.baseline_mic_24
import com.singing.config.track.TrackProperties
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.di.module.viewModels
import com.singing.audio.player.PlayerState
import org.singing.app.setup.collectAsStateSafe
import org.singing.app.ui.helper.Space
import org.singing.app.ui.screens.record.model.RecordState
import org.singing.app.ui.screens.record.views.audio.*
import org.singing.app.ui.screens.record.views.display.AudioDisplay
import org.singing.app.ui.screens.record.views.display.AudioDisplayData
import org.singing.app.ui.screens.record.views.filter.AudioInputFilters
import org.singing.app.ui.screens.record.views.filter.AudioInputFiltersActions
import org.singing.app.ui.screens.record.views.filter.AudioInputFiltersData
import org.singing.app.ui.screens.record.views.record.RecordHistory
import org.singing.app.ui.screens.record.views.replay.ReplayCard

actual class RecordScreen : Screen {
    private var _viewModel: RecordViewModel? = null
    private val viewModel get() = _viewModel!!

    @Composable
    override fun Content() {
        _viewModel = viewModels<RecordViewModel>()

        val verticalScroll = rememberScrollState()

        val recordData by viewModel.recordData.collectAsStateSafe()

        if (recordData.isAnyInputEnabled) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = verticalScroll)
                    .padding(
                        horizontal = 24.dp,
                        vertical = 16.dp
                    )
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(shape = MaterialTheme.shapes.medium)
                ) {
                    DisplayMainContainer()

                    Space(24.dp)

                    AudioLayoutContainer()

                    Space(16.dp)

                    RecordHistoryContainer()
                }

                Space(16.dp)

                Column(
                    modifier = Modifier
                        .width(324.dp)
                ) {
                    FiltersCardContainer()

                    Space(16.dp)

                    ReplayCardContainer()
                }
            }
        } else {
            NoVoiceInputLayout()
        }
    }

    @Composable
    private fun NoVoiceInputLayout() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .widthIn(min = 300.dp, max = 600.dp)
                    .clip(shape = MaterialTheme.shapes.medium)
                    .background(color = MaterialTheme.colorScheme.errorContainer)
                    .padding(24.dp, 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    modifier = Modifier.size(36.dp),
                    imageVector = vectorResource(Res.drawable.baseline_mic_24),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                )

                Space(8.dp)

                Text(
                    text = "Подключите микрофон",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = "Для работы приложения необходим микрофон",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }

    @Composable
    private fun DisplayMainContainer() {
        val state by viewModel.uiState.collectAsStateSafe()

        val recordData by viewModel.recordData.collectAsStateSafe()

        with(state) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 256.dp)
                    .clip(shape = RoundedCornerShape(36.dp))
                    .background(color = MaterialTheme.colorScheme.surface)
            ) {
                AudioDisplay(
                    modifier = Modifier
                        .fillMaxSize()
                        .let {
                            if (recordCountdown == null) it
                            else it.blur(16.dp)
                        },
                    data = AudioDisplayData(
                        pair = recordData.firstInput to recordData.secondInput,
                    ),
                    action = {
                        StartRecordButton(
                            enabled = state.recordCountdown == null,
                            isPlaying = recordState == RecordState.RECORD,
                            onClick = {
                                when (recordState) {
                                    RecordState.RECORD -> viewModel.stopRecord()
                                    RecordState.STOP -> viewModel.startRecord()
                                    RecordState.COUNTDOWN -> {}
                                }
                            }
                        )
                    }
                )

                if (state.recordCountdown != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                MaterialTheme.colorScheme
                                    .surfaceContainerHighest
                                    .copy(
                                        alpha = 0.01f
                                    )
                            )
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = state.recordCountdown.toString(),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.displayMedium,
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun AudioLayoutContainer() {
        val coroutineScope = rememberCoroutineScope()
        var showAudioTrackPicker by remember { mutableStateOf(false) }
        var showAudioProcessDialog by remember { mutableStateOf(false) }

        val state by viewModel.uiState.collectAsStateSafe()

        val audioProcessState = state.audioProcessState
        val selectedAudio = audioProcessState?.selectedAudio

        if (audioProcessState != null) {
            AudioProcessDialog(
                visible = showAudioProcessDialog,
                data = AudioProcessDialogData(
                    isParsing = audioProcessState.isParsing,
                    title = audioProcessState.selectedAudio.name,
                    result = audioProcessState.data,
                ),
                onCloseRequest = {
                    if (!audioProcessState.isParsing) {
                        showAudioProcessDialog = false
                    }
                }
            )
        }

        with(state) {
            FilePicker(
                show = showAudioTrackPicker,
                fileExtensions = TrackProperties.allowedSoundFormats,
                onFileSelected = { inputFile ->
                    coroutineScope.launch(Dispatchers.IO) {
                        showAudioTrackPicker = false

                        if (inputFile != null) {
                            showAudioProcessDialog = true
                            viewModel.processAudio(inputFile)
                        }
                    }
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = MaterialTheme.shapes.medium)
                    .background(color = MaterialTheme.colorScheme.tertiaryContainer)
                    .padding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    )
            ) {
                if (selectedAudio == null) {
                    SelectTrackFile(
                        enabled = canEditTrack && canEditPlayerState,
                        onTrackPickup = {
                            showAudioTrackPicker = true
                        }
                    )
                } else {
                    val duration = selectedAudio.duration

                    AudioTrackControls(
                        data = AudioTrackControlsData(
                            duration = duration,
                            currentPosition = playerPosition,
                            isPlaying = playerState == PlayerState.PLAY,
                            canEditPlayerState = canEditPlayerState,
                            canEditTrack = canEditTrack,
                        ),
                        actions = AudioTrackControlsActions(
                            onTrackPositionChange = {
                                viewModel.setPlayerPosition((duration * it).toLong())
                            },
                            onChangeTrack = {
                                showAudioTrackPicker = true
                            },
                            onRemoveTrack = {
                                viewModel.clearSelectedAudio()
                            },
                            onPreviewClick = {
                                if (playerState == PlayerState.STOP) {
                                    viewModel.startPlaying()
                                } else {
                                    viewModel.stopPlaying()
                                }
                            }
                        ),
                    )
                }
            }
        }
    }

    @Composable
    private fun RecordHistoryContainer() {
        val recordData by viewModel.recordData.collectAsStateSafe()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.medium)
                .background(color = MaterialTheme.colorScheme.surfaceContainerLow)
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                )
        ) {
            RecordHistory(
                items = recordData.history.asReversed(),
            )
        }
    }

    @Composable
    private fun FiltersCardContainer() {
        val state by viewModel.uiState.collectAsStateSafe()

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainer,
        ) {
            AudioInputFilters(
                data = AudioInputFiltersData(
                    items = state.filters,
                ),
                actions = AudioInputFiltersActions(
                    onFiltersUpdate = {
                        viewModel.setVoiceFilters(it)
                    }
                )
            )
        }
    }

    @Composable
    private fun ReplayCardContainer() {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.medium)
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            ReplayCard()
        }
    }
}
