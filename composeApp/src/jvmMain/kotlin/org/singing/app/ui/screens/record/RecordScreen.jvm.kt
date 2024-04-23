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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.baseline_mic_24
import com.singing.app.composeapp.generated.resources.baseline_volume_up_black_24dp
import com.singing.audio.utils.backgroundScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.di.module.viewModels
import org.singing.app.domain.audio.voice.VoiceInput
import org.singing.app.domain.model.audio.Frequency
import org.singing.app.setup.audio.AudioDefaults
import org.singing.app.setup.collectAsStateSafe
import org.singing.app.ui.helper.Space
import org.singing.app.ui.screens.record.model.PlayerUiState
import org.singing.app.ui.screens.record.model.RecorderState
import org.singing.app.ui.screens.record.views.audio.*
import org.singing.app.ui.screens.record.views.display.AudioDisplay
import org.singing.app.ui.screens.record.views.display.AudioDisplayData
import org.singing.app.ui.screens.record.views.filter.AudioInputFilters
import org.singing.app.ui.screens.record.views.filter.AudioInputFiltersActions
import org.singing.app.ui.screens.record.views.filter.AudioInputFiltersData
import org.singing.app.ui.screens.record.views.replay.ReplayCard

actual class RecordScreen : Screen {
    private var _viewModel: RecordViewModel? = null
    private val viewModel get() = _viewModel!!

    @Composable
    override fun Content() {
        _viewModel = viewModels<RecordViewModel>()

        val isEnabled by VoiceInput.isEnabled.collectAsStateSafe(true)

        val verticalScroll = rememberScrollState()

        if (isEnabled) {
            Row(
                modifier = Modifier
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

//                RecordHistoryContainer()
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
        val scope = rememberCoroutineScope()

        val selectedAudio by viewModel.selectedAudio.collectAsStateSafe()
        val recorderState by viewModel.recorderState.collectAsStateSafe()

        val currentMicrophoneFrequency by VoiceInput.voiceData
            .collectAsState(Frequency(0.0), Dispatchers.IO)

        val firstAudio =
            AudioDisplayData(
                icon = vectorResource(Res.drawable.baseline_mic_24),
                title = "C4",
                subtitle = "${currentMicrophoneFrequency.round()} Hz",
            )

        val secondAudio = selectedAudio?.let {
            AudioDisplayData(
                icon = vectorResource(Res.drawable.baseline_volume_up_black_24dp),
                title = "C4",
                subtitle = "237 Hz",
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 256.dp)
                .clip(shape = RoundedCornerShape(36.dp))
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            AudioDisplay(
                firstData = firstAudio,
                secondData = secondAudio,
                action = {
                    StartRecordButton(
                        isPlaying = recorderState != RecorderState.STOP,
                        onClick = {
                            scope.launch {
                                if (recorderState == RecorderState.STOP) {
                                    viewModel.startRecord()
                                } else {
                                    viewModel.stopRecord()
                                }
                            }
                        }
                    )
                }
            )
        }
    }

    @Composable
    private fun AudioLayoutContainer() {
        val scope = rememberCoroutineScope()

        val selectedAudio by viewModel.selectedAudio.collectAsStateSafe()
        val playerPosition by viewModel.playerPosition.collectAsStateSafe()
        val recorderState by viewModel.recorderState.collectAsStateSafe()
        val playerState by viewModel.playerState.collectAsStateSafe()

        val canEditPlayerState = recorderState != RecorderState.RECORD
        val canEditTrack = playerState != PlayerUiState.PLAY

        var showAudioTrackPicker by remember { mutableStateOf(false) }

        FilePicker(
            show = showAudioTrackPicker,
            fileExtensions = AudioDefaults.AllowedSoundFormats,
            onFileSelected = { inputFile ->
                backgroundScope.launch(Dispatchers.IO) {
                    showAudioTrackPicker = false

                    if (inputFile != null) {
                        viewModel.setSelectedAudio(inputFile)
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
                    enabled = canEditTrack,
                    onTrackPickup = {
                        showAudioTrackPicker = true
                    }
                )
            } else {
                val duration = selectedAudio!!.duration

                AudioTrackControls(
                    data = AudioTrackControlsData(
                        duration = duration,
                        currentPosition = playerPosition,
                        isPlaying = playerState == PlayerUiState.PLAY,
                        canEditPlayerState = canEditPlayerState,
                        canEditTrack = canEditTrack,
                    ),
                    actions = AudioTrackControlsActions(
                        onTrackPositionChange = {
                            scope.launch {
                                viewModel.setPlayerPosition((duration * it).toLong())
                            }
                        },
                        onChangeTrack = {
                            showAudioTrackPicker = true
                        },
                        onRemoveTrack = {
                            viewModel.clearSelectedAudio()
                        },
                        onPreviewClick = {
                            scope.launch {
                                if (playerState == PlayerUiState.STOP) {
                                    viewModel.startPlaying()
                                } else {
                                    viewModel.stopPlaying()
                                }
                            }
                        }
                    ),
                )
            }
        }
    }

    @Composable
    private fun FiltersCardContainer() {
        val filters by VoiceInput.voiceFilters.collectAsStateSafe()

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainer,
        ) {
            AudioInputFilters(
                data = AudioInputFiltersData(
                    items = filters,
                ),
                actions = AudioInputFiltersActions(
                    onFiltersUpdate = {
                        VoiceInput.setFilters(it)
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
