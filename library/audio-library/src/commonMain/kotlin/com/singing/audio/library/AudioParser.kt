package com.singing.audio.library

import com.singing.audio.library.decoder.AudioDecoder
import com.singing.audio.library.input.AudioInput
import com.singing.audio.library.looper.DecoderLoop
import com.singing.audio.library.params.AudioParams
import com.singing.audio.library.transformation.bytes.ByteOperation
import com.singing.audio.library.utils.isPowerOf2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AudioParser<T>(
    val data: AudioParams,
    input: AudioInput,
    val decoder: AudioDecoder<T>,
    val decoderLoop: DecoderLoop? = null
) {
    var input: AudioInput = input
        set(value) {
            synchronized(input) {
                field = value
            }
        }

    private val sampleValueRange by lazy {
        val minValue = -2 shl (data.sampleSizeInBits - 2)
        val maxValue = -minValue - 1

        minValue to maxValue
    }

    init {
        if (data.bufferSize <= 1) {
            throw IllegalArgumentException("Buffer size cannot be less or equals than 1")
        }

        if (!data.bufferSize.isPowerOf2()) {
            throw IllegalArgumentException("Buffer size must be power of 2")
        }
    }

    fun parse(
        onBytesRead: ((ByteArray) -> Unit)? = null,
    ): Flow<T> {
        return flow {
            val buf = ByteArray(data.bufferSize)

            input.init()
            decoder.start()
            decoderLoop?.onStart()

            while (
                synchronized(input) {
                    input.readBytes(buf)
                }
            ) {
                onBytesRead?.invoke(buf)

                val samples = computeSamples(buf)

                if (decoderLoop?.isResume(samples) == false) {
                    break
                }

                val value = decoder.decode(samples)

                emit(value)
            }
        }
    }

    private fun computeSamples(buf: ByteArray): FloatArray {
        val frameSize = data.frameSize
        val bufFrame = FloatArray(buf.size / frameSize)

        for (i in bufFrame.indices step frameSize) {
            val sample =
                if (data.isBigEndian) ByteOperation.byteToIntBigEndian(buf, i, frameSize)
                else ByteOperation.byteToIntLittleEndian(buf, i, frameSize)

            bufFrame[i / frameSize] = normalizeSample(sample)
        }

        return bufFrame
    }

    private fun normalizeSample(sample: Int): Float {
        if (data.frameSize == 2) {
            val minValue = sampleValueRange.first
            val maxValue = sampleValueRange.second

            val actualSample =
                if (sample > maxValue) sample + minValue + minValue
                else sample

            return actualSample.toFloat()
        } else {
            return sample.toFloat()
        }
    }
}