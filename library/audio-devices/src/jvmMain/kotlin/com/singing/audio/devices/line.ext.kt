package com.singing.audio.devices

import javax.sound.sampled.AudioFormat
import javax.sound.sampled.SourceDataLine
import javax.sound.sampled.TargetDataLine

fun TargetDataLine.tryOpen(format: AudioFormat?) =
    if (format == null) open()
    else open(format)

fun SourceDataLine.tryOpen(format: AudioFormat?) =
    if (format == null) open()
    else open(format)
