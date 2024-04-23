package org.singing.app.domain.model.audio

import kotlin.math.round

@JvmInline
value class Frequency(val data: Double) {
    fun round(): Int = round(data).toInt()
}
