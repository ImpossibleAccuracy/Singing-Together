package com.singing.audio.devices

import com.singing.audio.devices.model.AudioDevice
import com.singing.audio.devices.model.AudioDevicePlatformData
import com.singing.audio.devices.model.AudioDeviceType
import javax.sound.sampled.*

actual class DevicesScanner {
    companion object {
        var inputAudioFormat: AudioFormat? = null
        var outputAudioFormat: AudioFormat? = null
    }

    actual fun getAudioDevices(): List<AudioDevice> {
        val inputLines = mutableMapOf<Mixer, Line>()
        val outputLines = mutableMapOf<Mixer, Line>()

        var defaultInputLine: Line? = null
        var defaultOutputLine: Line? = null

        val mixers = AudioSystem.getMixerInfo()

        for (mixerInfo in mixers) {
            val mixer = AudioSystem.getMixer(mixerInfo)

            val targetLinesInfo = mixer.targetLineInfo
            if (targetLinesInfo.isNotEmpty() &&
                targetLinesInfo.first().lineClass == TargetDataLine::class.java
            ) {
                val lineInfo = targetLinesInfo.first()

                if (!AudioSystem.isLineSupported(lineInfo)) {
                    continue
                }

                val line = mixer.getLine(lineInfo) as TargetDataLine

                try {
                    line.tryOpen(line.format)
                } catch (e: Exception) {
                    System.err.println("Error on line \"${mixerInfo.name}\": ${e.message}")
                }

                inputLines[mixer] = line

                if (defaultInputLine == null) {
                    defaultInputLine = line
                }
            }

            val sourceLinesInfo = mixer.sourceLineInfo
            if (sourceLinesInfo.isNotEmpty() &&
                sourceLinesInfo.first().lineClass == SourceDataLine::class.java
            ) {
                val lineInfo = sourceLinesInfo.first()

                if (!AudioSystem.isLineSupported(lineInfo)) {
                    continue
                }

                val line = mixer.getLine(lineInfo) as SourceDataLine

                try {
                    line.tryOpen(outputAudioFormat)
                } catch (e: Exception) {
                    System.err.println("Error on line ${mixerInfo.name}: ${e.message}")
                }

                outputLines[mixer] = line

                if (defaultOutputLine == null) {
                    defaultOutputLine = line
                }
            }
        }

        val inputs = inputLines
            .map {
                AudioDevice(
                    title = it.key.mixerInfo.name.trim(),
                    type = AudioDeviceType.Input,
                    isDefault = it.value == defaultInputLine,
                    data = AudioDevicePlatformData(
                        mixer = it.key,
                        line = it.value
                    )
                )
            }

        val outputs = outputLines
            .map {
                AudioDevice(
                    title = it.key.mixerInfo.name.trim(),
                    type = AudioDeviceType.Output,
                    isDefault = it.value == defaultOutputLine,
                    data = AudioDevicePlatformData(
                        mixer = it.key,
                        line = it.value
                    )
                )
            }

        return inputs.plus(outputs)
    }
}
