package com.singing.audio.library.params

data class DecoderParams(
    val bufferSize: Int,
    val frameRate: Float,
    val frameSize: Int,
    val sampleRate: Float,
    val sampleSizeInBits: Int,
    val channels: Int,
    val isBigEndian: Boolean
)
