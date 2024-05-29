package com.singing.audio.taros.decoder.impl

import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.pitch.PitchDetectionResult
import com.singing.audio.taros.decoder.DecoderResult
import com.singing.audio.taros.decoder.TarosDspDecoder

class NullableTarosDspDecoder : TarosDspDecoder<Double?> {
    override fun transform(pitch: PitchDetectionResult, e: AudioEvent): DecoderResult<Double?> =
        if (pitch.isPitched && pitch.pitch > 0) DecoderResult.Data(pitch.pitch.toDouble())
        else DecoderResult.Data(null)
}
