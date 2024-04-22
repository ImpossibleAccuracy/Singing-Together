package com.singing.audio.library.input.ext

import com.singing.audio.library.params.DecoderParams
import javax.sound.sampled.AudioFormat

fun DecoderParams.toAudioFormat() =
    AudioFormat(
        AudioFormat.Encoding.PCM_SIGNED,
        sampleRate,
        sampleSizeInBits,
        channels,
        frameSize,
        frameRate,
        isBigEndian
    )
