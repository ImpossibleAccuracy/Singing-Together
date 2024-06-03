package com.singing.app.audio.compute

import com.singing.domain.model.PointAccuracy
import com.singing.domain.model.RecordPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.abs

// TODO: bad algorithm
suspend fun computeAccuracyByPoints(points: List<RecordPoint>): Double = withContext(Dispatchers.Default) {
    var skipped = 0

    var sum = 0

    for (point in points) {
        if (point.first == null || point.second == null) {
            skipped += 1
        } else {
            val diff = abs(point.first!! - point.second!!)
            val pointAccuracy = PointAccuracy.calculateAccuracy(diff.toFloat())

            sum += pointAccuracy.percent
        }
    }

    sum.toDouble() / (points.size - skipped)
}