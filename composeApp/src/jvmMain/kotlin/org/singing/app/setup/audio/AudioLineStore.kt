package org.singing.app.setup.audio

import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem

object AudioLineStore {
    fun openInput(audioFormat: AudioFormat) =
        try {
            AudioSystem
                .getTargetDataLine(audioFormat)
                .also {
                    it.open(audioFormat)
                }
        } catch (e: Exception) {
            e.printStackTrace()

            null
        }
}
