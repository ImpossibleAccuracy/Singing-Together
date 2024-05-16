package org.singing.app.domain.model

enum class PointAccuracy(val maxDiff: Float?) {
    Best(2f),
    Normal(5f),
    Bad(10f),
    Worst(10000f),
    Undefined(null);

    companion object {
        fun calculateAccuracy(diff: Float) =
            entries.find {
                it.maxDiff != null && it.maxDiff >= diff
            } ?: Undefined
    }
}
