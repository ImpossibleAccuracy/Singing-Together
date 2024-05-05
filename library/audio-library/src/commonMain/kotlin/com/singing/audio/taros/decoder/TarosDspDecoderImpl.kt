package com.singing.audio.taros.decoder

import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.pitch.PitchDetectionResult

class TarosDspDecoderImpl : TarosDspDecoder<Double> {
    override fun transform(pitch: PitchDetectionResult, e: AudioEvent): Double? =
        if (pitch.isPitched && pitch.pitch > 0) pitch.pitch.toDouble()
        else null
}
