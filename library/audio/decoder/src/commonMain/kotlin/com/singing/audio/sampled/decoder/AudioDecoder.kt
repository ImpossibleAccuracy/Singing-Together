package com.singing.audio.sampled.decoder

interface AudioDecoder<T> {
    fun start() {}

    fun decode(samples: DoubleArray): T
}
