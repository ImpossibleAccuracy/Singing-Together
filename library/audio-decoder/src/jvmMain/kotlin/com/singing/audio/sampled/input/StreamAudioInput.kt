package com.singing.audio.sampled.input

import javax.sound.sampled.AudioInputStream

class StreamAudioInput(
    private val stream: AudioInputStream
) : BaseAudioInput {
    override fun readBytes(buf: ByteArray): Boolean {
        return stream.read(buf) > 0
    }
}
