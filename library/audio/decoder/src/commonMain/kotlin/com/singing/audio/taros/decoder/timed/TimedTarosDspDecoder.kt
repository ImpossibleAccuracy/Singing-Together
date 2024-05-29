package com.singing.audio.taros.decoder.timed

import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.pitch.PitchDetectionResult
import com.singing.audio.sampled.model.TimedFrequency
import com.singing.audio.taros.decoder.DecoderResult
import com.singing.audio.taros.decoder.TarosDspDecoder
import kotlin.math.roundToLong

class TimedTarosDspDecoder : TarosDspDecoder<TimedFrequency> {
    override fun transform(pitch: PitchDetectionResult, e: AudioEvent): DecoderResult<TimedFrequency> =
        getFrequency(pitch)?.let {
            DecoderResult.Data(
                TimedFrequency(
                    frequency = it,
                    positionMs = (e.timeStamp * 1000).roundToLong()
                )
            )
        } ?: DecoderResult.NoData

    private fun getFrequency(pitch: PitchDetectionResult) =
        if (pitch.isPitched && pitch.pitch > 0) pitch.pitch.toDouble()
        else null
}
