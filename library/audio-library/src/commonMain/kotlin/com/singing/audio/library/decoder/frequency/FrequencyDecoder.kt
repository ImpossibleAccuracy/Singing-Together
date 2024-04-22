package com.singing.audio.library.decoder.frequency

import com.singing.audio.library.decoder.AudioDecoder
import com.singing.audio.library.params.DecoderParams
import com.singing.audio.library.transformation.fft.KotlinFFT
import com.singing.audio.library.transformation.frequency.FrequencyUtils

open class FrequencyDecoder(
    private val data: DecoderParams,
    bufferSize: Int,
) : AudioDecoder<Double> {
    private val fft by lazy {
        val numberOfSamples = bufferSize / data.frameSize

        KotlinFFT(numberOfSamples)
    }

    private val bins by lazy {
        FrequencyUtils.computeBins(bufferSize, data.frameSize, data.frameRate)
    }

    override fun decode(samples: FloatArray): Double {
        return FrequencyUtils.computeFrequency(samples, fft, bins)
    }
}

