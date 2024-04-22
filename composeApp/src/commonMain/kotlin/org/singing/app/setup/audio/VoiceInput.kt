package org.singing.app.setup.audio

import com.singing.audio.library.AudioParser
import com.singing.audio.library.input.AudioInput
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.singing.app.domain.model.AudioFilter
import org.singing.app.domain.model.Frequency

@OptIn(ExperimentalCoroutinesApi::class)
object VoiceInput {
    private val _voiceFilters = MutableStateFlow<List<AudioFilter>>(listOf())
    val voiceFilters = _voiceFilters.asStateFlow()

    private val parser: MutableStateFlow<AudioParser<Double>?> = MutableStateFlow(null)

    val voiceData: Flow<Frequency> = parser
        .filterNotNull()
        .flatMapConcat {
            it.parse()
        }
        .onEach {
//            println(it)
        }
        .map {
            Frequency(it)
        }

    val isEnabled: Flow<Boolean> = parser
        .map {
            it != null
        }

    init {
        GlobalScope.launch {
            val input = getVoiceInputStream()

            if (input != null) {
                setAudioInput(input)
            }
        }
    }

    fun setFilters(newFilters: List<AudioFilter>) {
        _voiceFilters.value = newFilters
    }

    suspend fun setAudioInput(input: AudioInput?) =
        withContext(Dispatchers.IO) {
            if (input == null) {
                parser.value = null

                return@withContext
            }

            val parserValue = parser.value

            if (parserValue == null) {
                parser.value =
                    AudioParser(
                        data = AudioDefaults.VoiceDecoderParams,
                        input = input,
                        decoder = createSimpleFrequencyDecoder(),
                    )
            } else {
                parserValue.input = input
            }
        }
}
