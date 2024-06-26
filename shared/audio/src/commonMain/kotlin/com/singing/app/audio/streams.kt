package com.singing.app.audio

import com.singing.audio.library.filter.AudioFilter
import com.singing.audio.library.parser.AudioParser
import com.singing.audio.sampled.model.TimedFrequency
import com.singing.app.base.ComposeFile

expect suspend fun createVoiceAudioParser(filters: List<AudioFilter>): AudioParser<Double?>?

expect suspend fun createVoiceAudioParser(file: ComposeFile, filters: List<AudioFilter>): AudioParser<TimedFrequency>

expect suspend fun createTrackAudioParser(file: ComposeFile, filters: List<AudioFilter>): AudioParser<TimedFrequency>
