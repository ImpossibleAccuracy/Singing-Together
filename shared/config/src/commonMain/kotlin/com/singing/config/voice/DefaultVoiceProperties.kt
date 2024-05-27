package com.singing.config.voice

import com.singing.audio.library.filter.AudioFilter
import com.singing.audio.taros.filter.HighPassAudioFilter
import com.singing.audio.taros.filter.LowPassAudioFilter

internal object DefaultVoiceProperties : IVoiceProperties {
    override val inputBufferSize = 1024 * 16
    override val inputSampleRate = 41400

    override val defaultFilters: List<AudioFilter> = listOf(
        LowPassAudioFilter("Default LowPass Filter", 1200F, inputSampleRate.toFloat()),
        HighPassAudioFilter("Default HighPass Filter", 100f, inputSampleRate.toFloat()),
    )
}