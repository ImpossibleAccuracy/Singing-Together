package com.singing.audio.taros

import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.io.TarsosDSPAudioFormat
import be.tarsos.dsp.io.UniversalAudioInputStream
import java.io.InputStream
import javax.sound.sampled.AudioFormat

internal fun createAudioDispatcher(file: InputStream, format: AudioFormat, bufferSize: Int) =
    AudioDispatcher(
        UniversalAudioInputStream(
            file,
            TarsosDSPAudioFormat(
                TarsosDSPAudioFormat.Encoding.PCM_SIGNED,
                format.sampleRate,
                format.sampleSizeInBits,
                format.channels,
                format.frameSize,
                format.frameRate,
                format.isBigEndian,
            )
        ),
        bufferSize,
        0,
    )
