package org.singing.app.domain.store.voice

import com.singing.app.audio.createVoiceAudioParser
import com.singing.audio.library.parser.AudioParser
import com.singing.audio.utils.backgroundScope
import com.singing.config.voice.VoiceProperties
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
object VoiceInput {
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
        }
    }

    suspend fun reloadParser() {
        val newParser = createVoiceAudioParser(VoiceProperties.defaultFilters)
        parser.value = newParser
        isParserEmittedAtLeastOnce.value = true
    }
}
