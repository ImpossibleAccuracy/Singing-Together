package com.singing.feature.recording.domain.data

import com.singing.app.audio.createVoiceAudioParser
import com.singing.app.domain.model.TrackParseResult
import com.singing.config.voice.VoiceProperties
import com.singing.feature.recording.domain.InputListener
import com.singing.feature.recording.domain.InputListener.LineParams
import com.singing.feature.recording.domain.InputListener.MergeStrategy
import com.singing.feature.recording.domain.model.AudioInputData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope


sealed interface LineState {
    data class Enabled(val value: Double?) : LineState

    data object Disabled : LineState

    data object NotInit : LineState
}


class InputListenerImpl : InputListener {
    private val _audioInputData = MutableStateFlow<AudioInputData?>(null)
    override val audioInputData: Flow<AudioInputData> = _audioInputData.filterNotNull()

    private val firstInput = MutableStateFlow<LineState>(LineState.NotInit)
    private val secondInput = MutableStateFlow<LineState>(LineState.NotInit)


    override suspend fun init(
        strategy: MergeStrategy,
        lineParams: LineParams,
    ) {
        supervisorScope {
            launch(Dispatchers.Default) {
                handleVoiceInput()
            }

            launch(Dispatchers.Default) {
                handleAudioInput(
                    lineParams.audioTrackFlow.distinctUntilChanged(),
                    lineParams.audioPositionFlow.distinctUntilChanged(),
                )
            }

            launch(Dispatchers.Default) {
                collectInputs(strategy)
            }
        }
    }

    private suspend fun handleVoiceInput() {
        firstInput.value = LineState.NotInit

        val voiceParser = createVoiceAudioParser(VoiceProperties.defaultFilters)

        if (voiceParser == null) {
            firstInput.value = LineState.Disabled
        } else {
            voiceParser.parse().collect {
                firstInput.value = LineState.Enabled(it)
            }
        }
    }

    private suspend fun handleAudioInput(
        selectedAudioFlow: Flow<TrackParseResult?>,
        audioPositionFlow: Flow<Long>
    ) {
        secondInput.value = LineState.NotInit

        selectedAudioFlow
            .distinctUntilChanged()
            .collect { audioState ->
                when (audioState) {
                    null -> {
                        secondInput.value = LineState.Disabled
                    }

                    else -> {
                        audioPositionFlow.collect { playerPosition ->
                            secondInput.value = LineState.Enabled(
                                audioState.data.entries
                                    .firstOrNull {
                                        it.key >= playerPosition
                                    }?.value
                            )
                        }
                    }
                }
            }
    }

    private suspend fun collectInputs(strategy: MergeStrategy) = supervisorScope {
        val mergedFlows = mergeFlows(strategy, firstInput, secondInput)

        mergedFlows.transform { (first, second) ->
            if (first is LineState.Disabled) {
                _audioInputData.value = AudioInputData(isEnabled = false)
                return@transform
            }

            val firstLineValue = when (first) {
                is LineState.Enabled -> first.value
                else -> null
            }

            val secondLineValue = when (second) {
                is LineState.Enabled -> second.value
                else -> null
            }

            emit(firstLineValue to secondLineValue)
        }.collect { (first, second) ->
            _audioInputData.value = AudioInputData(
                isEnabled = true,
                firstInput = first,
                secondInput = second,
            )
        }
    }

    private fun <T> mergeFlows(strategy: MergeStrategy, flow1: Flow<T>, flow2: Flow<T>): Flow<Pair<T, T>> =
        when (strategy) {
            MergeStrategy.Zip -> flow1.zip(flow2) { f, s -> f to s }
            MergeStrategy.Combine -> flow1.combine(flow2) { f, s -> f to s }
        }
}