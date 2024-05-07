package org.singing.app.ui.screens.record

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.singing.app.composeapp.generated.resources.*
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.di.module.viewModels
import org.singing.app.setup.collectAsStateSafe
import org.singing.app.ui.base.AppScreen
import org.singing.app.ui.base.Space
import org.singing.app.ui.base.formatFrequency
import org.singing.app.ui.base.formatTimeString
import org.singing.app.ui.screens.record.viewmodel.RecordViewModel
import org.singing.app.ui.screens.record.viewmodel.model.RecordItem
import org.singing.app.ui.screens.record.viewmodel.model.RecordPair
import org.singing.app.ui.screens.record.viewmodel.state.AudioProcessState
import org.singing.app.ui.views.FilledButton
import kotlin.math.roundToLong

class RecordScreen(
    private val audio: AudioProcessState? = null,
    private var isNewInstance: Boolean = true,
) : AppScreen() {
    private var _viewModel: RecordViewModel? = null
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
        _viewModel = viewModels<RecordViewModel>()

        val verticalScrollState = rememberScrollState()

        LaunchedEffect(audio) {
            if (isNewInstance) {
                if (audio == null) {
                    viewModel.clearSelectedAudio()
                } else {
                    viewModel.setProcessedAudio(audio)
                }
            }
        }

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


    @Composable
    private fun DisplayContainer() {
        val recordData by viewModel.recordData.collectAsStateSafe()
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
            AudioPlayer(
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
        )
    }


    @Composable
    private fun Display(
        firstInput: RecordItem?,
        secondInput: RecordItem?,
        recordCountdown: Int?,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(256.dp)
                .clip(shape = RoundedCornerShape(36.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .let {
                        if (recordCountdown == null) it
                        else it.blur(16.dp)
                    }
            ) {
                val firstInputColor = MaterialTheme.colorScheme.primary
                val secondInputColor = MaterialTheme.colorScheme.secondary

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .let {
                            when (secondInput == null) {
                                true -> it.background(firstInputColor)

                                false -> it.background(
                                    brush = Brush.linearGradient(
                                        0.4f to firstInputColor,
                                        0.6f to secondInputColor,
                                    )
                                )
                            }
                        }
                ) {
                    if (firstInput != null) {
                        InputLineDisplay(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(shape = MaterialTheme.shapes.extraLarge),
                            note = firstInput.note,
                            frequency = formatFrequency(firstInput.frequency),
                            icon = vectorResource(Res.drawable.baseline_mic_24),
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        )
                    }

                    if (secondInput != null) {
                        InputLineDisplay(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(shape = MaterialTheme.shapes.extraLarge),
                            note = secondInput.note,
                            frequency = formatFrequency(secondInput.frequency),
                            icon = vectorResource(Res.drawable.baseline_volume_up_black_24dp),
                            contentColor = MaterialTheme.colorScheme.onSecondary,
                        )
                    }
                }
            }

            if (recordCountdown != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            MaterialTheme.colorScheme
                                .inverseSurface
                                .copy(
                                    alpha = 0.4F
                                )
                        )
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = recordCountdown.toString(),
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                        style = MaterialTheme.typography.displayMedium,
                    )
                }
            }
        }
    }

    @Composable
    private fun InputLineDisplay(
        modifier: Modifier = Modifier,
        note: String,
        frequency: String,
        icon: ImageVector,
        contentColor: Color,
    ) {
        Column(
            modifier = modifier
                .padding(
                    horizontal = 24.dp,
                    vertical = 16.dp,
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier.size(size = 24.dp),
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
            )

            Space(8.dp)

            Text(
                text = note,
                color = contentColor,
                style = TextStyle(
                    fontSize = 45.sp,
                    fontWeight = FontWeight.Black
                )
            )

            Text(
                text = frequency,
                color = contentColor,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light
                )
            )
        }
    }

    @Composable
    private fun AudioPlayer(
        editable: Boolean,
        totalDuration: Long,
        currentPosition: Long,
        isPlaying: Boolean,
        onPositionChange: (Long) -> Unit,
        onPlay: () -> Unit,
        onStop: () -> Unit,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.large)
                .background(color = MaterialTheme.colorScheme.secondary)
                .padding(
                    start = 16.dp,
                    top = 18.dp,
                    end = 16.dp,
                    bottom = 12.dp,
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = formatTimeString(currentPosition),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                )

                Space(4.dp)

                Slider(
                    enabled = editable,
                    modifier = Modifier.weight(1f),
                    value = (currentPosition.toFloat() / totalDuration),
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.secondaryContainer,
                        activeTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                        inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    ),
                    onValueChange = {
                        onPositionChange((it * totalDuration).roundToLong())
                    },
                )

                Space(4.dp)

                Text(
                    text = formatTimeString(totalDuration),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                )
            }

            Space(12.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                val actionText = when (isPlaying) {
                    true -> stringResource(Res.string.action_stop_playing)
                    false -> stringResource(Res.string.action_start_playing)
                }

                FilledButton(
                    modifier = Modifier.widthIn(min = 180.dp),
                    enabled = editable,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    label = actionText,
                    onClick = {
                        if (isPlaying) {
                            onStop()
                        } else {
                            onPlay()
                        }
                    }
                )
            }
        }
    }

    @Composable
    private fun DisplayInfo(
        isRecording: Boolean,
        canRecord: Boolean,
        recordDuration: Long,
        onRecordStart: () -> Unit,
        onRecordFinish: () -> Unit,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .padding(
                    horizontal = 24.dp,
                    vertical = 16.dp
                )
        ) {
            if (isRecording) {
                FilledButton(
                    label = stringResource(Res.string.action_stop_record),
                    startIcon = vectorResource(Res.drawable.baseline_stop_black_24dp),
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = onRecordFinish,
                )
            } else {
                FilledButton(
                    enabled = canRecord,
                    label = stringResource(Res.string.action_start_record),
                    startIcon = vectorResource(Res.drawable.baseline_radio_button_checked_black_24dp),
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = onRecordStart,
                )
            }

            Spacer(Modifier.weight(1f))

            if (isRecording) {
                Text(
                    text = formatTimeString(recordDuration),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                )
            } else {
                Text(
                    text = stringResource(Res.string.label_start_record),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }

    @Composable
    private fun RecordHistory(
        history: List<RecordPair>
    ) {
        var isHistoryVisible by remember { mutableStateOf(true) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                )
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(Res.string.label_recording),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleLarge,
                )

                Spacer(Modifier.weight(1f))

                IconButton(
                    onClick = {
                        isHistoryVisible = !isHistoryVisible
                    }
                ) {
                    if (isHistoryVisible) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.baseline_visibility_24),
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = "",
                        )
                    } else {
                        Icon(
                            imageVector = vectorResource(Res.drawable.baseline_visibility_off_24),
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = "",
                        )
                    }
                }
            }

            Space(8.dp)

            AnimatedVisibility(
                visible = isHistoryVisible
            ) {
                if (history.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = stringResource(Res.string.label_no_history),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                        )
                    }
                } else {
                    Column {
                        history.forEachIndexed { index, it ->
                            val first = it.first
                            val second = it.second

                            if (first == null && second == null) return@forEachIndexed

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(shape = MaterialTheme.shapes.small)
                                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
                                    .padding(
                                        horizontal = 12.dp,
                                        vertical = 8.dp
                                    )
                            ) {
                                Text(
                                    text = formatTimeString(it.time),
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    style = MaterialTheme.typography.labelMedium,
                                )

                                Space(12.dp)

                                if (first != null) {
                                    Text(
                                        text = first.note,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        style = MaterialTheme.typography.bodyLarge,
                                    )
                                }

                                if (second != null) {
                                    Space(4.dp)

                                    Text(
                                        text = "(${second.note})",
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        style = MaterialTheme.typography.titleMedium,
                                    )
                                }

                                Spacer(Modifier.weight(1f))

                                if (first != null) {
                                    Text(
                                        text = formatFrequency(first.frequency),
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        style = MaterialTheme.typography.labelLarge,
                                    )
                                }

                                if (second != null) {
                                    Space(4.dp)

                                    Text(
                                        text = "(${formatFrequency(second.frequency)})",
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        style = MaterialTheme.typography.labelMedium,
                                    )
                                }
                            }

                            if (index != history.lastIndex) {
                                Space(8.dp)
                            }
                        }
                    }
                }
            }
        }
    }
}
