package com.singing.audio.library.input

import com.singing.audio.library.input.ext.toAudioFormat
import com.singing.audio.library.params.AudioParams
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.TargetDataLine

class DataLineAudioInput(
    private val dataLine: TargetDataLine,
    private val audioParams: AudioParams,
) : AudioInput {
    private val stream = AudioInputStream(dataLine)

    override fun init() {
        start()
    }

    private fun start() {
        try {
            if (!dataLine.isOpen) {
                dataLine.open(audioParams.toAudioFormat())
            }

            dataLine.start()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override fun readBytes(buf: ByteArray): Boolean {
        if (!dataLine.isRunning) {
            start()
        }

        return stream.read(buf) > 0
    }
}