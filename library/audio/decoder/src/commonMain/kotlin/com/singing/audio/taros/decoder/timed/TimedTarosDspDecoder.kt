package com.singing.audio.taros.decoder.timed

import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.pitch.PitchDetectionResult
import com.singing.audio.sampled.model.TimedFrequency
import com.singing.audio.taros.decoder.TarosDspDecoder
import kotlin.math.roundToLong

class TimedTarosDspDecoder : TarosDspDecoder<TimedFrequency> {
    override fun transform(pitch: PitchDetectionResult, e: AudioEvent): TimedFrequency? =
        getFrequency(pitch)?.let {
            TimedFrequency(
                frequency = it,
                positionMs = (e.timeStamp * 1000).roundToLong()
            )
        }

    private fun getFrequency(pitch: PitchDetectionResult) =
        if (pitch.isPitched && pitch.pitch > 0) pitch.pitch.toDouble()
        else null
}
