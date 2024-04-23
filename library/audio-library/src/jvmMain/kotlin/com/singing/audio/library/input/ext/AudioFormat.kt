package com.singing.audio.library.input.ext

import com.singing.audio.library.params.AudioParams
import javax.sound.sampled.AudioFormat

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
