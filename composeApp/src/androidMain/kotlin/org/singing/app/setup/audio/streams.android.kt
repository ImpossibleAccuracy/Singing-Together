package org.singing.app.setup.audio

import com.singing.audio.library.filter.AudioFilter
import com.singing.audio.library.parser.AudioParser
import com.singing.audio.player.model.AudioFile
import com.singing.audio.sampled.model.TimedFrequency

actual suspend fun createVoiceAudioParser(filters: List<AudioFilter>): AudioParser<Double>? {
    TODO("Not yet implemented")
}

actual suspend fun createTrackAudioParser(file: AudioFile, filters: List<AudioFilter>): AudioParser<TimedFrequency> {
    TODO("Not yet implemented")
}