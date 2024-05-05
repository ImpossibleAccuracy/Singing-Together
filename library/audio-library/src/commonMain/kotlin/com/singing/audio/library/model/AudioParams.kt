package com.singing.audio.library.model

data class AudioParams(
    val bufferSize: Int,
    val frameRate: Float,
    val frameSize: Int,
    val sampleRate: Float,
    val sampleSizeInBits: Int,
    val channels: Int,
    val isBigEndian: Boolean
)
