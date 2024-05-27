package com.singing.domain.model

import androidx.compose.runtime.Stable
import kotlin.math.abs

@Stable
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
