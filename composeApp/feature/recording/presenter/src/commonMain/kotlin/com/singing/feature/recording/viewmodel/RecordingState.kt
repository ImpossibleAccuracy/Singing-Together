package com.singing.feature.recording.viewmodel

import com.singing.app.domain.model.TrackParseResult
import com.singing.app.domain.model.UserData
import com.singing.domain.model.RecordPoint
import com.singing.feature.recording.domain.model.RecordState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class RecordingState(
    val user: UserData? = null,

    val recordState: RecordState = RecordState.STOP,
    val recordCountdown: Int? = null,
    val recordStartedAt: Long? = null,

    val playerPosition: Long = 0,
    val isPlaying: Boolean = false,

    val trackData: TrackParseResult? = null,
    val history: PersistentList<RecordPoint> = persistentListOf(),
)

