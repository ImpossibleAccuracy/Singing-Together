package com.singing.audio.library.input.ext

import com.singing.audio.sampled.input.BaseAudioInput
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.TargetDataLine

class DataLineAudioInput(
    private val dataLine: TargetDataLine
) : BaseAudioInput {
    private val stream = AudioInputStream(dataLine)

    private fun start() {
        dataLine.open()
        dataLine.start()
    }

    private fun stop() {
        dataLine.stop()
        dataLine.close()
    }

    override fun readBytes(buf: ByteArray): Boolean {
        if (!dataLine.isOpen) {
            start()
        }

        return stream.read(buf) > 0
    }
}
