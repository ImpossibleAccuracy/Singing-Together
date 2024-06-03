package com.singing.config.track

import com.singing.audio.library.filter.AudioFilter
import com.singing.audio.taros.filter.HighPassAudioFilter
import com.singing.audio.taros.filter.LowPassAudioFilter
import com.singing.config.voice.DefaultVoiceProperties.inputSampleRate

internal object DefaultTrackProperties : ITrackProperties {
    override val bufferSize: Int = 1024 * 16

    override val defaultFilters: List<AudioFilter> = listOf(
        LowPassAudioFilter("Default LowPass Filter", 1200F, inputSampleRate.toFloat()),
        HighPassAudioFilter("Default HighPass Filter", 80f, inputSampleRate.toFloat()),
    )

    override val allowedSoundFormats: Map<String, List<String>> = mapOf(
        "m4a" to listOf("audio/mp4"),
        "mp3" to listOf("audio/mpeg"),
        "wav" to listOf(
            "audio/wav",
            "audio/wave",
            "audio/x-wave",
        ),
        "wma" to listOf("audio/x-ms-wma"),
        "aac" to listOf("audio/aac"),
    )
}