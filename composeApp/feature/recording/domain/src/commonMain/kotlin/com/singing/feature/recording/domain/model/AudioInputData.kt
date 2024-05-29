package com.singing.feature.recording.domain.model

data class AudioInputData(
    val isEnabled: Boolean = true,

    val firstInput: Double? = null,
    val secondInput: Double? = null,
)
