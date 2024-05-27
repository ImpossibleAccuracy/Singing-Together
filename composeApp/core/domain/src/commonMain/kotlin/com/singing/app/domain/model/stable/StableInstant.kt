package com.singing.app.domain.model.stable

import androidx.compose.runtime.Stable
import kotlinx.datetime.Instant

@Stable
@JvmInline
value class StableInstant(
    val instant: Instant
)
