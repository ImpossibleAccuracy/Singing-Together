package com.singing.audio.audio

import javazoom.spi.mpeg.sampled.file.MpegEncoding
import java.io.BufferedInputStream
import java.io.InputStream
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem

fun openAudioStream(input: InputStream): AudioInputStream {
    val originalAudioInputStream = AudioSystem.getAudioInputStream(
        BufferedInputStream(input)
    )

    val originalAudioFormat = originalAudioInputStream.format

    return when (originalAudioFormat.encoding) {
        is MpegEncoding -> {
            val targetAudioFormat = AudioFormat(
                originalAudioFormat.sampleRate,
                16,
                originalAudioFormat.channels,
                true,
                false
            )

            AudioSystem.getAudioInputStream(
                targetAudioFormat,
                originalAudioInputStream,
            )
        }

        else -> originalAudioInputStream
    }
}

fun convertChannels(input: AudioInputStream, channels: Int): AudioInputStream {
    val originalAudioFormat = input.format

    if (originalAudioFormat.channels == channels) {
        return input
    }

    val targetFormat = AudioFormat(
        originalAudioFormat.encoding,
        originalAudioFormat.sampleRate,
        originalAudioFormat.sampleSizeInBits,
        channels,
        ((originalAudioFormat.sampleSizeInBits + 7) / 8) * channels,
        originalAudioFormat.frameRate,
        originalAudioFormat.isBigEndian
    )

    return AudioSystem.getAudioInputStream(targetFormat, input)
}
