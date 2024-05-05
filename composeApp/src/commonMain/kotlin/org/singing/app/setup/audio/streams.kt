package org.singing.app.setup.audio

import com.singing.audio.library.filter.AudioFilter
import com.singing.audio.library.parser.AudioParser
import com.singing.audio.sampled.model.TimedFrequency
import com.singing.audio.player.model.AudioFile

expect suspend fun createVoiceAudioParser(filters: List<AudioFilter>): AudioParser<Double>?

expect suspend fun createTrackAudioParser(file: AudioFile, filters: List<AudioFilter>): AudioParser<TimedFrequency>
