package com.singing.audio.library.input

import com.singing.audio.library.input.AudioInput
import javax.sound.sampled.AudioInputStream

class StreamAudioInput(
    private val stream: AudioInputStream
) : AudioInput {
    override fun readBytes(buf: ByteArray): Boolean {
        return stream.read(buf) > 0
    }
}
