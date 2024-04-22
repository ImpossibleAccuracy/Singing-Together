package org.singing.app.ui.helper

import kotlin.math.floor
import kotlin.math.round

fun getTimeString(value: Long): String {
    val seconds = value / 1000.0
    val minutes = seconds / 60.0

    val actualMinutes = floor(minutes).toInt()
    val actualSeconds = (round(seconds) - actualMinutes * 60).toInt()

    val secondsTime = actualSeconds.toString().padStart(2, '0')
    val minutesTime = actualMinutes.toString().padStart(2, '0')

    return "$minutesTime:$secondsTime"
}
