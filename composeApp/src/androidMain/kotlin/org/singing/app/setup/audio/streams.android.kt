package org.singing.app.setup.audio

import com.singing.audio.library.filter.AudioFilter
import com.singing.audio.library.parser.AudioParser
import org.singing.app.domain.model.AudioFile
import com.singing.audio.sampled.model.TimedFrequency
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

actual suspend fun createVoiceAudioParser(filters: List<AudioFilter>): AudioParser<Double>? {
    return object : AudioParser<Double> {
        override fun parse(): Flow<Double> {
            val items = listOf(100.0, 200.0, 300.0, 400.0, 500.0)

            return flow {
                while (true) {
                    for (item in items) {
                        emit(item)

                        delay(1500)
                    }
                }
            }
        }
    }
}

actual suspend fun createTrackAudioParser(audioFile: AudioFile, filters: List<AudioFilter>): AudioParser<TimedFrequency> {
    return object : AudioParser<TimedFrequency> {
        override fun parse(): Flow<TimedFrequency> {
            return flowOf()
        }
    }
}