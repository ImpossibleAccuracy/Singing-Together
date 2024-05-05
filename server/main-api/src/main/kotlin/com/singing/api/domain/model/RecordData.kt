package com.singing.api.domain.model

data class RecordData(
    val recordItems: Map<Long, Pair<Double, Double?>>,
    val duration: Long,
)