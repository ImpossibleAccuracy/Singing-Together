package com.singing.audio.library.decoder

interface AudioDecoder<T> {
    fun start() {}

    fun decode(samples: FloatArray): T
}
