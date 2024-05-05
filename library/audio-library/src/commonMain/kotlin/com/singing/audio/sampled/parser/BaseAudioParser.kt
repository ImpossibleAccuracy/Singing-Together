package com.singing.audio.sampled.parser

import com.singing.audio.library.model.AudioParams
import com.singing.audio.library.parser.AudioParser
import com.singing.audio.sampled.decoder.AudioDecoder
import com.singing.audio.sampled.input.BaseAudioInput
import com.singing.audio.sampled.transformation.bytes.ByteOperation
import com.singing.audio.sampled.utils.isPowerOf2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.math.pow

class BaseAudioParser<T>(
    private val params: AudioParams,
    private val input: BaseAudioInput,
    private val decoder: AudioDecoder<T>,
) : AudioParser<T> {
    private val sampleValueRange by lazy {
        val minValue = -2 shl (params.sampleSizeInBits - 2)
        val maxValue = -minValue - 1

        minValue to maxValue
    }

    init {
        if (params.bufferSize <= 1) {
            throw IllegalArgumentException("Buffer size cannot be less or equals than 1")
        }

        if (!params.bufferSize.isPowerOf2()) {
            throw IllegalArgumentException("Buffer size must be power of 2")
        }
    }

    override fun parse(): Flow<T> {
        return flow {
            val buf = ByteArray(params.bufferSize * params.channels)

            input.init()
            decoder.start()

            while (input.readBytes(buf)) {
                val samples = computeSamples(buf)

                val value = decoder.decode(samples)

                emit(value)
            }
        }
    }

    private fun computeSamples(buf: ByteArray): DoubleArray {
        val samples = ByteOperation.parseSamples(buf, params.frameSize, params.sampleSizeInBits)

        for (i in samples.indices) {
            samples[i] = samples[i] / 2.0.pow(params.sampleSizeInBits.toDouble() - 1)
        }

        val joinSamples = ByteOperation.joinSamples(samples, params.channels)

        return joinSamples
    }

    private fun normalizeSample(sample: Double): Double {
        if (params.frameSize == 2) {
            val minValue = sampleValueRange.first
            val maxValue = sampleValueRange.second

            val actualSample =
                if (sample > maxValue) sample + minValue + minValue
                else sample

            return actualSample
        } else {
            return sample
        }
    }
}