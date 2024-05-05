@file:Suppress("unused")

package com.singing.audio.library.input.ext

import be.tarsos.dsp.io.TarsosDSPAudioFormat
import com.singing.audio.library.model.AudioParams
import javax.sound.sampled.AudioFormat

fun AudioFormat.toAudioParams(bufferSize: Int) =
    AudioParams(
        bufferSize = bufferSize,
        frameRate = frameRate,
        frameSize = frameSize,
        sampleRate = sampleRate,
        sampleSizeInBits = sampleSizeInBits,
        channels = channels,
        isBigEndian = isBigEndian
    )

fun AudioParams.toAudioFormat() =
    AudioFormat(
        AudioFormat.Encoding.PCM_SIGNED,
        sampleRate,
        sampleSizeInBits,
        channels,
        frameSize,
        frameRate,
        isBigEndian
    )


fun TarsosDSPAudioFormat.toAudioParams(bufferSize: Int) =
    AudioParams(
        bufferSize = bufferSize,
        frameRate = frameRate,
        frameSize = frameSize,
        sampleRate = sampleRate,
        sampleSizeInBits = sampleSizeInBits,
        channels = channels,
        isBigEndian = isBigEndian
    )

fun AudioParams.toTarosDspAudioFormat() =
    TarsosDSPAudioFormat(
        TarsosDSPAudioFormat.Encoding.PCM_SIGNED,
        sampleRate,
        sampleSizeInBits,
        channels,
        frameSize,
        frameRate,
        isBigEndian
    )
