package org.singing.app.domain.model

import kotlin.math.abs

data class RecordPoint(
    val time: Long,
    val first: Double,
    val second: Double?,
) {
    val accuracy: PointAccuracy by lazy {
        if (second == null) return@lazy PointAccuracy.Undefined

        val diff = abs(first - second).toFloat()

        PointAccuracy.calculateAccuracy(diff)
    }
}
