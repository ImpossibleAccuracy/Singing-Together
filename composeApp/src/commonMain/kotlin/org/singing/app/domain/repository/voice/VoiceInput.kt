package org.singing.app.domain.repository.voice

import com.singing.audio.library.filter.AudioFilter
import com.singing.audio.library.parser.AudioParser
import com.singing.audio.utils.backgroundScope
import com.singing.config.voice.VoiceProperties
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.singing.app.setup.audio.createVoiceAudioParser

@OptIn(ExperimentalCoroutinesApi::class)
object VoiceInput {
    private val _voiceFilters = MutableStateFlow(VoiceProperties.defaultFilters)
    val voiceFilters = _voiceFilters.asStateFlow()

    private var isParserEmittedAtLeastOnce = MutableStateFlow(false)
    private val parser = MutableStateFlow<AudioParser<Double>?>(null)

    val voiceData: Flow<Double> = parser
        .filterNotNull()
        .flatMapConcat { audioParser ->
//            flowOf<Double>()
            audioParser.parse()
        }
        .shareIn(backgroundScope, SharingStarted.Lazily)

    val isEnabled: Flow<Boolean> = parser
        .combine(isParserEmittedAtLeastOnce) { parser, isEmitted ->
            !isEmitted || parser != null
        }
        .distinctUntilChanged()

    init {
        backgroundScope.launch {
            reloadParser()

            voiceFilters
                .drop(1)
                .collect {
                    reloadParser()
                }
        }
    }

    suspend fun reloadParser() {
        val newParser = createVoiceAudioParser(_voiceFilters.value)
        parser.value = newParser
        isParserEmittedAtLeastOnce.value = true
    }

    fun setFilters(newFilters: List<AudioFilter>) {
        _voiceFilters.value = newFilters
    }
}
