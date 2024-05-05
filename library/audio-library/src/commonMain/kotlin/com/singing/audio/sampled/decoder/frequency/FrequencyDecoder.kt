package com.singing.audio.sampled.decoder.frequency

import com.singing.audio.library.model.AudioParams
import com.singing.audio.sampled.decoder.AudioDecoder
import com.singing.audio.sampled.transformation.fft.KotlinFFT
import com.singing.audio.sampled.transformation.frequency.FrequencyUtils

open class FrequencyDecoder(
    private val data: AudioParams,
) : AudioDecoder<Double> {
    private val fft by lazy {
        val numberOfSamples = data.bufferSize / data.frameSize

        KotlinFFT(numberOfSamples)
    }

    private val bins by lazy {
        FrequencyUtils.computeBins(data.bufferSize, data.frameSize, data.frameRate)
    }

    override fun decode(samples: DoubleArray): Double {
        return FrequencyUtils.computeFrequency(samples, fft, bins)
    }
}
