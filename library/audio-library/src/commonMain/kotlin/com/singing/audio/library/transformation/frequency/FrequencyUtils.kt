package com.singing.audio.library.transformation.frequency

import com.singing.audio.library.transformation.fft.KotlinFFT
import com.singing.audio.library.utils.indexOfMax
import kotlin.math.hypot

object FrequencyUtils {
    fun computeBins(bufferSize: Int, frameSize: Int, frameRate: Float): DoubleArray {
        val samplesCount = bufferSize / frameSize
        val binWidth = frameRate.toDouble() / samplesCount

        return (0..<samplesCount)
            .map {
                it * binWidth
            }
            .toDoubleArray()
    }

    fun computeFrequency(samples: FloatArray, fft: KotlinFFT, bins: DoubleArray): Double {
        val transformed = fft.transform(samples)

        val realPart = transformed[0]
        val imaginaryPart = transformed[1]

        val magnitudes = computeMagnitudes(realPart, imaginaryPart)

        val maxMagnitude = magnitudes.toTypedArray().indexOfMax()

        return bins[maxMagnitude]
    }

    fun computeMagnitudes(realPart: FloatArray, imaginaryPart: FloatArray): DoubleArray {
        val powers = DoubleArray(realPart.size / 2)

        for (i in powers.indices) {
            powers[i] = hypot(realPart[i].toDouble(), imaginaryPart[i].toDouble())
        }

        return powers
    }
}