package org.singing.app.ui.screens.record.audio

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.singing.app.composeapp.generated.resources.*
import com.singing.audio.utils.ComposeFile
import com.singing.config.note.NotesStore
import com.singing.config.track.TrackProperties
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.di.module.viewModels
import org.singing.app.domain.model.RecentTrack
import org.singing.app.setup.audio.createTrackAudioParser
import org.singing.app.setup.audio.processAudioFile
import org.singing.app.setup.collectAsStateSafe
import org.singing.app.setup.file.FilePicker
import org.singing.app.ui.base.Space
import org.singing.app.ui.base.formatTimeString
import org.singing.app.ui.screens.record.create.RecordingScreen
import org.singing.app.ui.screens.record.create.viewmodel.model.RecordItem
import org.singing.app.ui.screens.record.create.viewmodel.state.AudioProcessState
import org.singing.app.ui.views.base.AppFilledButton
import org.singing.app.ui.views.base.track.TrackListItem

class SelectAudioScreen : Screen {
    companion object {
        private val extensions = TrackProperties.allowedSoundFormats.toImmutableList()
    }

    @Composable
    override fun Content() {
        val viewModel = viewModels<SelectAudioViewModel>()
        val coroutineScope = rememberCoroutineScope()
        val navigator = LocalNavigator.currentOrThrow

        val recentTracks by viewModel.recentFavouriteTracks.collectAsStateSafe()
        val isTracksLoading by viewModel.isRecentFavouriteTracksLoading.collectAsStateSafe()

        var audioProcessState by remember { mutableStateOf<AudioProcessState?>(null) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer),
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .widthIn(min = 400.dp, max = 750.dp)
                    .clip(shape = MaterialTheme.shapes.medium)
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(36.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(36.dp),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
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


                val audioProcessStateCapture = audioProcessState

                if (audioProcessStateCapture == null) {
                    SelectAudioViewContainer(
                        coroutineScope = coroutineScope,
                        recentTracks = recentTracks,
                        isTracksLoading = isTracksLoading,
                        onAudioProcessStateChange = {
                            audioProcessState = it
                        },
                    )
                } else if (audioProcessStateCapture.isParsing) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    SelectedAudioInfo(
                        audioProcessState = audioProcessStateCapture,
                        onResetState = {
                            audioProcessState = null
                        },
                        navigateNext = {
                            navigator.replace(
                                RecordingScreen(
                                    audio = audioProcessState
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    @Composable
    private fun SelectedAudioInfo(
        audioProcessState: AudioProcessState,
        onResetState: () -> Unit,
        navigateNext: () -> Unit,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            InfoItem(
                key = "${stringResource(Res.string.label_file_name)}:",
                value = audioProcessState.selectedAudio.name,
            )

            InfoItem(
                key = "${stringResource(Res.string.label_file_full_path)}:",
                value = audioProcessState.selectedAudio.file.fullPath,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
        ) {
            AppFilledButton(
                modifier = Modifier.widthIn(min = 180.dp),
                label = stringResource(Res.string.action_replace),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                onClick = onResetState,
            )

            AppFilledButton(
                modifier = Modifier.widthIn(min = 180.dp),
                label = stringResource(Res.string.action_next),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                onClick = navigateNext,
            )
        }
    }

    @Composable
    private fun SelectAudioViewContainer(
        coroutineScope: CoroutineScope,
        recentTracks: ImmutableList<RecentTrack>,
        isTracksLoading: Boolean,
        onAudioProcessStateChange: (AudioProcessState) -> Unit,
    ) {
        SelectAudioView(
            recentTracks = recentTracks,
            isTracksLoading = isTracksLoading,
        ) { inputFile ->
            coroutineScope.launch(Dispatchers.IO) {
                processAudio(inputFile).collect {
                    onAudioProcessStateChange(it)
                }
            }
        }
    }

    @Composable
    private fun SelectAudioView(
        recentTracks: ImmutableList<RecentTrack>,
        isTracksLoading: Boolean,
        onFileSelected: (ComposeFile) -> Unit,
    ) {
        var showAudioTrackPicker by remember { mutableStateOf(false) }

        FilePicker(
            show = showAudioTrackPicker,
            fileExtensions = extensions,
            onFileSelected = { inputFile ->
                if (inputFile != null) {
                    onFileSelected(inputFile)
                }
            }
        )

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            FilledTonalButton(
                modifier = Modifier
                    .widthIn(400.dp, 700.dp)
                    .heightIn(min = 64.dp),
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                onClick = {
                    showAudioTrackPicker = true
                }
            ) {
                Text(
                    text = stringResource(Res.string.action_select_track),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.weight(1f))

                Icon(
                    imageVector = vectorResource(Res.drawable.baseline_folder_open_black_24dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    contentDescription = "",
                )
            }

            Space(24.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                HorizontalDivider(Modifier.weight(1f))

                Text(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = "OR",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleMedium,
                )

                HorizontalDivider(Modifier.weight(1f))
            }

            Space(24.dp)

            if (isTracksLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            } else {
                Text(
                    text = "Pick from recent",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelMedium,
                )

                Space(8.dp)

                if (recentTracks.isEmpty()) {
                    Text(
                        text = "No saved tracks",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium,
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 250.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(recentTracks) { track ->
                            TrackListItem(
                                modifier = Modifier
                                    .clip(shape = MaterialTheme.shapes.medium)
                                    .clickable {
                                        onFileSelected(track.audioFile.file)
                                    }
                                    .padding(horizontal = 12.dp),
                                filename = track.audioFile.name,
                                duration = formatTimeString(track.audioFile.duration),
                                isFavourite = track.isFavourite,
                                onFavouriteChange = {}
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun InfoItem(
        key: String,
        value: String,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = key,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
            )

            Space(8.dp)

            Text(
                text = value,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelLarge,
                maxLines = 1,
            )
        }
    }

    private fun processAudio(inputFile: ComposeFile) = flow {
        val audioFile = processAudioFile(inputFile)
            ?: throw IllegalArgumentException("Cannot process file: $inputFile")

        emit(
            AudioProcessState(
                selectedAudio = audioFile,
                isParsing = true,
            )
        )

        val parser = createTrackAudioParser(audioFile, TrackProperties.defaultFilters)

        val data = parser
            .parse()
            .map {
                RecordItem(
                    note = NotesStore.findNote(it.frequency),
                    frequency = it.frequency,
                    time = it.positionMs,
                )
            }
            .toList()

        emit(
            AudioProcessState(
                selectedAudio = audioFile,
                data = data.toImmutableList(),
                isParsing = false,
            )
        )
    }.flowOn(Dispatchers.IO)
}
