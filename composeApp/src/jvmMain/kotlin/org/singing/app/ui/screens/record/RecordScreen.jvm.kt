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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.mohamedrejeb.calf.core.LocalPlatformContext
import com.mohamedrejeb.calf.picker.FilePickerFileType
import com.mohamedrejeb.calf.picker.FilePickerSelectionMode
import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.baseline_mic_24
import com.singing.app.composeapp.generated.resources.baseline_volume_up_black_24dp
import kotlinx.coroutines.Dispatchers
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.di.module.viewModels
import org.singing.app.domain.model.AudioFile
import org.singing.app.domain.model.Frequency
import org.singing.app.domain.model.ListWithSelected
import org.singing.app.domain.model.PlayerState
import org.singing.app.setup.audio.VoiceInput
import org.singing.app.setup.collectAsStateSafe
import org.singing.app.ui.helper.Space
import org.singing.app.ui.screens.record.views.AudioInOutLayout
import org.singing.app.ui.screens.record.views.AudioInOutLayoutActions
import org.singing.app.ui.screens.record.views.AudioInOutLayoutData
import org.singing.app.ui.screens.record.views.audio.*
import org.singing.app.ui.screens.record.views.display.AudioDisplay
import org.singing.app.ui.screens.record.views.display.AudioDisplayData
import org.singing.app.ui.screens.record.views.filter.AudioInputFilters
import org.singing.app.ui.screens.record.views.filter.AudioInputFiltersActions
import org.singing.app.ui.screens.record.views.filter.AudioInputFiltersData
import org.singing.app.ui.screens.record.views.messages.NoVoiceInputMessage
import org.singing.app.ui.screens.record.views.replay.ReplayCard

actual class RecordScreen : Screen {
    private var _viewModel: RecordViewModel? = null
    private val viewModel get() = _viewModel!!

    private var _audioViewModel: RecordScreenAudioViewModel? = null
    private val audioViewModel get() = _audioViewModel!!

    @Composable
    override fun Content() {
        _viewModel = viewModels<RecordViewModel>()
        _audioViewModel = viewModels<RecordScreenAudioViewModel>()

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
                    AudioInOutLayoutContainer()

                    Space(16.dp)

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
        val selectedAudio by viewModel.selectedAudio.collectAsStateSafe()
        val playerState by viewModel.playerState.collectAsStateSafe()

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
                        isPlaying = playerState == PlayerState.PLAY,
                        onClick = {
                            viewModel.togglePlaying()
                        }
                    )
                }
            )
        }
    }

    @Composable
    private fun AudioLayoutContainer() {
        val selectedAudio by viewModel.selectedAudio.collectAsStateSafe()
        val playerPosition by viewModel.playerPosition.collectAsStateSafe()
        val playerState by viewModel.playerState.collectAsStateSafe()

        val pickerLauncher = rememberFilePickerLauncher(
            type = FilePickerFileType.Audio,
            selectionMode = FilePickerSelectionMode.Single,
            onResult = { files ->
                if (files.isNotEmpty()) {
                    viewModel.selectedAudio.value = AudioFile(
                        file = files.first()
                    )
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
                    onTrackPickup = {
                        pickerLauncher.launch()
                    }
                )
            } else {
                val duration = selectedAudio!!.duration
                val canEditPlayerData = playerState != PlayerState.PLAY

                AudioTrackControls(
                    data = AudioTrackControlsData(
                        duration = duration,
                        currentPosition = playerPosition,
                        isPlaying = playerState == PlayerState.PLAY_PREVIEW,
                        isEditable = canEditPlayerData,
                    ),
                    actions = AudioTrackControlsActions(
                        onTrackPositionChange = {
                            viewModel.updatePlayerPosition((duration * it).toLong())
                        },
                        onChangeTrack = {
                            pickerLauncher.launch()
                        },
                        onRemoveTrack = {
                            viewModel.selectedAudio.value = null
                        },
                        onPreviewClick = {
                            if (playerState == PlayerState.STOP) {
                                viewModel.startPlaying(true)
                            } else {
                                viewModel.stopPlaying()
                            }
                        }
                    ),
                )
            }
        }
    }

    @Composable
    private fun AudioInOutLayoutContainer() {
        val selectedAudio by viewModel.selectedAudio.collectAsStateSafe()
        val hasSelectedAudio = selectedAudio != null

        val selectedVoiceInput by audioViewModel.selectedVoiceInput.collectAsStateSafe()
        val selectedVoiceOutput by audioViewModel.selectedVoiceOutput.collectAsStateSafe()
        val selectedAudioOutput by audioViewModel.selectedAudioOutput.collectAsStateSafe()

        val microphones by audioViewModel.microphones.collectAsStateSafe()
        val speakers by audioViewModel.speakers.collectAsStateSafe()

        when {
            selectedVoiceInput == null -> {
                NoVoiceInputMessage()
            }

            else -> {
                AudioInOutLayout(
                    data = AudioInOutLayoutData(
                        defaultNoChoiceLabel = "Не выбрано",
                        showAudioOutput = hasSelectedAudio,
                        voiceInputs = ListWithSelected(
                            list = microphones,
                            selectedItem = selectedVoiceInput
                        ),
                        voiceOutputs = ListWithSelected(
                            list = speakers,
                            selectedItem = selectedVoiceOutput
                        ),
                        audioOutputs = ListWithSelected(
                            list = speakers,
                            selectedItem = selectedAudioOutput
                        ),
                    ),
                    actions = AudioInOutLayoutActions(
                        onVoiceInputSelected = {
                            audioViewModel.selectedVoiceInput.value = it
                        },
                        onVoiceOutputSelected = {
                            audioViewModel.selectedVoiceOutput.value = it
                        },
                        onAudioOutputSelected = {
                            audioViewModel.selectedAudioOutput.value = it
                        },
                    )
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
