package org.singing.app.setup.audio

import com.singing.audio.library.filter.AudioFilter
import com.singing.audio.library.parser.AudioParser
import org.singing.app.domain.model.AudioFile
import com.singing.audio.sampled.model.TimedFrequency

expect suspend fun createVoiceAudioParser(filters: List<AudioFilter>): AudioParser<Double>?

expect suspend fun createTrackAudioParser(audioFile: AudioFile, filters: List<AudioFilter>): AudioParser<TimedFrequency>
