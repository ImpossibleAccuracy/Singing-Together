package org.singing.app.domain.audio.voice

import com.singing.audio.library.AudioParser
import com.singing.audio.library.input.AudioInput
import com.singing.audio.utils.backgroundScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.singing.app.domain.model.audio.AudioFilter
import org.singing.app.domain.model.audio.Frequency
import org.singing.app.setup.audio.AudioDefaults
import org.singing.app.setup.audio.createSimpleFrequencyDecoder
import org.singing.app.setup.audio.getSoundInput

@OptIn(ExperimentalCoroutinesApi::class)
object VoiceInput {
    private val _voiceFilters = MutableStateFlow<List<AudioFilter>>(listOf())
    val voiceFilters = _voiceFilters.asStateFlow()

    private val _audioInput = MutableStateFlow<AudioInput?>(null)
    val audioInput = _audioInput.asStateFlow()

    private val parser: MutableStateFlow<AudioParser<Double>?> = MutableStateFlow(null)

    val voiceData: Flow<Frequency> = parser
        .filterNotNull()
        .flatMapConcat { audioParser ->
            audioParser.parse()
        }
        .map {
            Frequency(it)
        }

    val isEnabled: Flow<Boolean> = parser
        .map {
            it != null
        }

    init {
        backgroundScope.launch {
            launch {
                initParser()
            }

            launch {
                listenForInputUpdates()
            }
        }
    }

    fun setFilters(newFilters: List<AudioFilter>) {
        _voiceFilters.value = newFilters
    }

    fun setAudioInput(input: AudioInput?) {
        _audioInput.value = input
    }

    private fun initParser() {
        val input = getSoundInput()

        if (input != null) {
            setAudioInput(input)
        }
    }

    private suspend fun listenForInputUpdates() {
        _audioInput
            .collect { input ->
                if (input == null) {
                    parser.value = null
                    return@collect
                }

                val parserValue = parser.value

                if (parserValue == null) {
                    parser.value = createParser(input)
                } else {
                    parserValue.input = input
                }
            }
    }

    private fun createParser(
        input: AudioInput = _audioInput.value!!,
        filters: List<AudioFilter> = _voiceFilters.value,
    ): AudioParser<Double> {
        return AudioParser(
            data = AudioDefaults.VoiceInputAudioParams,
            input = input,
            decoder = createSimpleFrequencyDecoder(),
        )
    }
}
