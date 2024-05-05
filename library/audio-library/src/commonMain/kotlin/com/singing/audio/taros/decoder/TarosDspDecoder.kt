package com.singing.audio.taros.decoder

import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.pitch.PitchDetectionResult

interface TarosDspDecoder<T> {
    fun init() {}

    fun transform(pitch: PitchDetectionResult, e: AudioEvent): T?
}
