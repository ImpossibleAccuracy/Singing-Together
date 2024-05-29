package com.singing.feature.recording.domain.data

import com.singing.feature.recording.domain.ClockFlow
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

class ClockFlowImpl : ClockFlow {
    override val time: Flow<Long> = flow {
        while (currentCoroutineContext().isActive) {
            emit(System.currentTimeMillis())

            delay(100)
        }
    }
}
