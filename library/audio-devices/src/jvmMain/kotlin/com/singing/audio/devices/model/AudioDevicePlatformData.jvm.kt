package com.singing.audio.devices.model

import javax.sound.sampled.Line
import javax.sound.sampled.Mixer
import javax.sound.sampled.TargetDataLine

actual class AudioDevicePlatformData(
    val mixer: Mixer,
    val line: Line
) {
    val targetDataLine: TargetDataLine
        get() {
            return line as TargetDataLine
        }
}
