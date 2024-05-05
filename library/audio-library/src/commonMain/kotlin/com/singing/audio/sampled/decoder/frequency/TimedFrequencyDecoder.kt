package com.singing.audio.sampled.decoder.frequency

import com.singing.audio.library.model.AudioParams
import com.singing.audio.sampled.decoder.AudioDecoder
import com.singing.audio.sampled.model.TimedFrequency
import com.singing.audio.sampled.transformation.fft.KotlinFFT
import com.singing.audio.sampled.transformation.frequency.FrequencyUtils

open class TimedFrequencyDecoder(
    private val data: AudioParams,
) : AudioDecoder<TimedFrequency> {
    private val fft by lazy {
        val numberOfSamples = data.bufferSize / data.frameSize

        KotlinFFT(numberOfSamples)
    }

    private val bins by lazy {
        FrequencyUtils.computeBins(data.bufferSize, data.frameSize, data.frameRate)
    }

    private var playedTime: Long = 0

    override fun start() {
        playedTime = 0
    }

    override fun decode(samples: DoubleArray): TimedFrequency {
        val frequency = FrequencyUtils.computeFrequency(samples, fft, bins)

        val playedTimeSeconds = samples.size / data.frameRate

        playedTime += (playedTimeSeconds * 1000).toLong()

        return TimedFrequency(
            frequency = frequency,
            positionMs = playedTime
        )
    }
}

