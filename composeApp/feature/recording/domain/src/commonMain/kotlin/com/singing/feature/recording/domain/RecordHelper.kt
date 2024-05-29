package com.singing.feature.recording.domain

import com.singing.feature.recording.domain.model.RecordResult
import com.singing.feature.recording.domain.model.RecordState
import kotlinx.coroutines.flow.Flow

interface RecordHelper {
    val recordState: Flow<RecordState>

    val countdown: Flow<Int?>

    val recordResult: Flow<RecordResult?>

    suspend fun startRecord()

    fun stopRecordCountdown()

    suspend fun stopRecord()
}
