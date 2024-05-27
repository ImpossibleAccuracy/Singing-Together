package com.singing.config.voice

import com.singing.audio.library.filter.AudioFilter

actual object VoiceProperties : IVoiceProperties {
    override val inputBufferSize: Int = DefaultVoiceProperties.inputBufferSize
    override val inputSampleRate: Int = DefaultVoiceProperties.inputSampleRate
    override val defaultFilters: List<AudioFilter> = DefaultVoiceProperties.defaultFilters
}