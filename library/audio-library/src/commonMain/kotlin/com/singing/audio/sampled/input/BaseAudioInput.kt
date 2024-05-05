package com.singing.audio.sampled.input

import com.singing.audio.library.input.AudioInput

interface BaseAudioInput : AudioInput {
    fun init() {}

    fun readBytes(buf: ByteArray): Boolean
}