package com.singing.audio.sampled.transformation.bytes

object ByteOperation {
    fun byteToIntLittleEndian(buf: ByteArray, offset: Int, bytesPerSample: Int): Int {
        var sample = 0
        for (byteIndex in 0 until bytesPerSample) {
            val aByte = buf[offset + byteIndex].toInt() and 0xff
            sample += aByte shl 8 * (byteIndex)
        }
        return sample
    }

    fun byteToIntBigEndian(buf: ByteArray, offset: Int, bytesPerSample: Int): Int {
        var sample = 0
        for (byteIndex in 0 until bytesPerSample) {
            val aByte = buf[offset + byteIndex].toInt() and 0xff
            sample += aByte shl (8 * (bytesPerSample - byteIndex - 1))
        }
        return sample
    }

    fun joinSamples(samples: DoubleArray, channels: Int): DoubleArray {
        if (channels == 1) return samples

        val newSamples = DoubleArray(samples.size / channels)

        for (i in newSamples.indices) {
            var newSample = 0.0

            for (j in 0..<channels) {
                newSample += samples[i * channels + j]
            }

            newSamples[i] = newSample
        }

        return newSamples
    }

    fun parseSamples(buf: ByteArray, frameSize: Int, sampleSizeInBits: Int): DoubleArray {
        val samples = DoubleArray(buf.size / frameSize)

        for (i in samples.indices) {
            when (sampleSizeInBits) {
                8 -> {
                    samples[i] = buf[i].toDouble()
                }

                16 -> {
                    samples[i] = ((buf[2 * i + 1].toInt() shl 8) or buf[2 * i].toInt()).toDouble()
                }

                24 -> {
                    samples[i] =
                        ((buf[3 * i + 2].toInt() shl 16) or (buf[3 * i + 1].toInt() shl 8) or buf[3 * i].toInt()).toDouble()
                }

                32 -> {
                    samples[i] =
                        ((buf[4 * i + 3].toInt() shl 24) or (buf[4 * i + 2].toInt() shl 16) or (buf[4 * i + 1].toInt() shl 8) or buf[4 * i].toInt()).toDouble()
                }
            }
        }

        return samples
    }
}
