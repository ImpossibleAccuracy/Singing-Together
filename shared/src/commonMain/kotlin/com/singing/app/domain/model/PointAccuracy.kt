package com.singing.app.domain.model

enum class PointAccuracy(
    val maxDiff: Float?,
    val percent: Short,
) {
    Best(2f, 100),
    Normal(5f, 90),
    Bad(10f, 40),
    Worst(10000f, 0),
    Undefined(null, -100);

    companion object {
        fun calculateAccuracy(diff: Float) =
            entries.find {
                it.maxDiff != null && it.maxDiff >= diff
            } ?: Undefined
    }
}
