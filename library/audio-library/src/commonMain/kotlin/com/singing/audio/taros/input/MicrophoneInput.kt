package com.singing.audio.taros.input

import be.tarsos.dsp.io.jvm.AudioDispatcherFactory

fun createMicrophoneInput(
    sampleRate: Int,
    bufferSize: Int,
): TarosDspInput =
    TarosDspInput(
        AudioDispatcherFactory.fromDefaultMicrophone(sampleRate, bufferSize, 0)
    )
