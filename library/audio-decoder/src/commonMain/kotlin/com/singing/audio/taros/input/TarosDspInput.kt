package com.singing.audio.taros.input

import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.io.TarsosDSPAudioFormat
import com.singing.audio.library.input.AudioInput

class TarosDspInput(
    val dispatcher: AudioDispatcher
) : AudioInput {
    val format: TarsosDSPAudioFormat
        get() = dispatcher.format

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TarosDspInput

        return format == other.format
    }

    override fun hashCode(): Int {
        return format.hashCode()
    }
}
