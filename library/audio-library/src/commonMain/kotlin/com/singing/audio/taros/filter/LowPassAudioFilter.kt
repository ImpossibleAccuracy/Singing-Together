package com.singing.audio.taros.filter

import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.filters.LowPassFS

data class LowPassAudioFilter(
    override val title: String,
    val freq: Float,
    val sampleRate: Float,
) : TarosAudioFilter {
    override fun toDispatcher(): AudioProcessor =
        LowPassFS(freq, sampleRate)
}
