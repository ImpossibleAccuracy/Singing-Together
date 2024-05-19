package org.singing.app.ui.base

import kotlin.math.floor
import kotlin.math.roundToInt

fun formatTimeString(milliseconds: Long): String {
    val seconds = milliseconds / 1000.0
    val minutes = seconds / 60.0

    val actualMinutes = floor(minutes).roundToInt()
    val actualSeconds = (floor(seconds).roundToInt() - actualMinutes * 60)

    val secondsTime = actualSeconds.toString().padStart(2, '0')
    val minutesTime = actualMinutes.toString().padStart(2, '0')

    return "$minutesTime:$secondsTime"
}

fun formatFrequency(frequency: Double): String =
    "${frequency.roundToInt()} Hz"
