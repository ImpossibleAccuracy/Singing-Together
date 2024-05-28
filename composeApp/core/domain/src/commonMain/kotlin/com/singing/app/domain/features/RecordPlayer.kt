package com.singing.app.domain.features

import com.singing.app.domain.model.RecordData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface RecordPlayer {
    val isPlaying: Flow<Boolean>
    val position: StateFlow<Long>

    suspend fun play(recordData: RecordData)

    suspend fun reset()

    suspend fun setPosition(newPosition: Long)

    suspend fun stop()
}
