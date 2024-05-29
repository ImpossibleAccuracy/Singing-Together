package com.singing.feature.recording.domain

import kotlinx.coroutines.flow.Flow

interface ClockFlow {
    val time: Flow<Long>
}