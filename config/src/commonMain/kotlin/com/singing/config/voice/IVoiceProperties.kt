package com.singing.config.voice

import com.singing.audio.library.filter.AudioFilter

internal interface IVoiceProperties {
    val inputBufferSize: Int
    val inputSampleRate: Int
    val defaultFilters: List<AudioFilter>
}