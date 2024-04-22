package com.singing.audio.library.input

fun interface AudioInput {
    fun init() {}

    fun readBytes(buf: ByteArray): Boolean
}
