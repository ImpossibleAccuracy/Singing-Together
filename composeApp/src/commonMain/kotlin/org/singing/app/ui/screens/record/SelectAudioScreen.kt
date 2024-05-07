package org.singing.app.ui.screens.record

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.singing.audio.utils.backgroundScope
import com.singing.config.note.NotesStore
import com.singing.config.track.TrackProperties
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.setup.audio.createTrackAudioParser
import org.singing.app.setup.audio.processAudioFile
import org.singing.app.setup.file.ComposeFile
import org.singing.app.setup.file.FilePicker
import org.singing.app.ui.base.Space
import org.singing.app.ui.screens.record.viewmodel.model.RecordItem
import org.singing.app.ui.screens.record.viewmodel.state.AudioProcessState
import org.singing.app.ui.views.FilledButton

class SelectAudioScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        var audioProcessState by remember { mutableStateOf<AudioProcessState?>(null) }
        var showAudioTrackPicker by remember { mutableStateOf(false) }

        FilePicker(
            show = showAudioTrackPicker,
            fileExtensions = TrackProperties.allowedSoundFormats,
            onFileSelected = { inputFile ->
                backgroundScope.launch(Dispatchers.IO) {
                    showAudioTrackPicker = false

                    if (inputFile != null) {
                        processAudio(inputFile).collect {
                            audioProcessState = it
                        }
                    }
                }
            }
        )

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
            ) {
                Text(
                    text = stringResource(Res.string.title_select_track),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.displayMedium,
                    textAlign = TextAlign.Center,
                )

                Space(4.dp)

                Text(
                    text = stringResource(Res.string.subtitle_select_track),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )

                Space(36.dp)

                when {
                    audioProcessState?.isParsing == true -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }

                    audioProcessState == null -> {
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
                    }

                    else -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            InfoItem(
                                key = "${stringResource(Res.string.label_file_name)}:",
                                value = audioProcessState!!.selectedAudio.name,
                            )

                            InfoItem(
                                key = "${stringResource(Res.string.label_file_full_path)}:",
                                value = audioProcessState!!.selectedAudio.path,
                            )
                        }

                        Space(36.dp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                        ) {
                            FilledButton(
                                modifier = Modifier.widthIn(min = 180.dp),
                                label = stringResource(Res.string.action_replace),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                onClick = {
                                    showAudioTrackPicker = true
                                }
                            )

                            Space(12.dp)

                            FilledButton(
                                modifier = Modifier.widthIn(min = 180.dp),
                                label = stringResource(Res.string.action_next),
                                color = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                onClick = {
                                    navigator.replace(
                                        RecordScreen(
                                            audio = audioProcessState
                                        )
                                    )
                                }
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
                data = data,
                isParsing = false,
            )
        )
    }
}
