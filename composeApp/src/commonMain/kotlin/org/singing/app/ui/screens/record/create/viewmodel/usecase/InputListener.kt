package org.singing.app.ui.screens.record.create.viewmodel.usecase

import com.singing.config.note.NotesStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.singing.app.domain.model.RecordPoint
import org.singing.app.domain.store.voice.VoiceInput
import org.singing.app.ui.screens.record.create.viewmodel.model.RecordItem
import org.singing.app.ui.screens.record.create.viewmodel.state.AudioInputData
import org.singing.app.ui.screens.record.create.viewmodel.state.AudioProcessState

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class InputListener(
    private val coroutineScope: CoroutineScope,
    private val recordOffsetTime: () -> Long?,
    private val selectedAudioFlow: Flow<AudioProcessState?>,
    private val playerPositionFlow: Flow<Long>,
    private val onNewHistoryItem: (RecordPoint) -> Unit,
) {
    companion object {
        private val DefaultRecordItem = NotesStore.default().let {
            RecordItem(
                frequency = it.frequency,
                note = it.note,
                time = 0,
            )
        }
    }

    private val _firstInput = MutableStateFlow<Flow<RecordItem>?>(null)
    private val _secondInput = MutableStateFlow<Flow<RecordItem>?>(null)

    private val _audioInputData = MutableStateFlow(AudioInputData())
    val audioInputData = _audioInputData.asStateFlow()

    init {
        listenForInputs()
        listenForVoiceInput()
        listenForSelectedAudio()
    }

    private fun listenForInputs() = coroutineScope.launch {
        launch {
            val first = _firstInput.flatMapConcat { it ?: flowOf(null) }
            val second = _secondInput.flatMapConcat { it ?: flowOf(null) }

            first.combine(second) { f, s ->
                f to s
            }.collect { (f, s) ->
                _audioInputData.update {
                    it.copy(
                        firstInput = f,
                        secondInput = s,
                    )
                }
            }
        }

        launch {
            audioInputData
                .map { it.firstInput to it.secondInput }
                .distinctUntilChanged()
                .sample(1000)
                .collect { pair ->
                    val time = recordOffsetTime()

                    if (time != null) {
                        onNewHistoryItem(
                            RecordPoint(
                                first = pair.first!!.frequency,
                                second = pair.second?.frequency,
                                time = time,
                            )
                        )
                    }
                }
        }
    }

    private fun listenForVoiceInput() = coroutineScope.launch {
        VoiceInput.isEnabled.collect { isEnabled ->
            _audioInputData.update {
                it.copy(
                    isAnyInputEnabled = isEnabled,
                )
            }

            when (isEnabled) {
                true -> {
                    _firstInput.value = VoiceInput.voiceData
                        .map {
                            buildRecordItem(
                                frequency = it,
                                time = recordOffsetTime() ?: -1
                            )
                        }
                        .stateIn(
                            coroutineScope,
                            SharingStarted.Lazily,
                            DefaultRecordItem,
                        )
                }

                false -> {
                    _firstInput.value = null
                }
            }
        }
    }

    private fun listenForSelectedAudio() = coroutineScope.launch {
        selectedAudioFlow
            .distinctUntilChanged()
            .collect { audioState ->
                _secondInput.value = when {
                    audioState?.data == null || audioState.isParsing -> null

                    // When player position changes, when will new note be emitted
                    else -> playerPositionFlow.map { playerPosition ->
                        audioState.data.firstOrNull {
                            it.time >= playerPosition
                        } ?: DefaultRecordItem
                    }
                }
            }
    }

    private fun buildRecordItem(frequency: Double, time: Long) =
        RecordItem(
            note = NotesStore.findNote(frequency),
            frequency = frequency,
            time = time
        )
}
