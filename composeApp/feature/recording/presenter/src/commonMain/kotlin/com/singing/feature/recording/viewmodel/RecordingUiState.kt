package com.singing.feature.recording.viewmodel

import com.singing.app.domain.model.TrackParseResult
import com.singing.app.domain.model.UserData
import com.singing.domain.model.RecordPoint
import com.singing.feature.recording.domain.model.RecordState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

sealed interface RecordingUiState {
    companion object {
        fun fromViewModelState(state: RecordingState): RecordingUiState =
            when (state.recordState) {
                RecordState.RECORD -> Recording(
                    recordStartedAt = state.recordStartedAt!!,
                    user = state.user,
                    playerPosition = state.playerPosition,
                    isPlaying = state.isPlaying,
                    trackData = state.trackData,
                    history = state.history,
                )

                RecordState.COUNTDOWN -> Countdown(
                    recordCountdown = state.recordCountdown,
                    user = state.user,
                    playerPosition = state.playerPosition,
                    isPlaying = state.isPlaying,
                    trackData = state.trackData,
                    history = state.history,
                )

                RecordState.STOP -> Stopped(
                    user = state.user,
                    playerPosition = state.playerPosition,
                    isPlaying = state.isPlaying,
                    trackData = state.trackData,
                    history = state.history,
                )
            }
    }

    val user: UserData?
    val playerPosition: Long
    val isPlaying: Boolean
    val trackData: TrackParseResult?
    val history: PersistentList<RecordPoint>

    data class Recording(
        val recordStartedAt: Long,
        override val user: UserData? = null,
        override val playerPosition: Long = 0,
        override val isPlaying: Boolean = false,
        override val trackData: TrackParseResult? = null,
        override val history: PersistentList<RecordPoint> = persistentListOf(),
    ) : RecordingUiState

    data class Countdown(
        val recordCountdown: Int?,
        override val user: UserData? = null,
        override val playerPosition: Long = 0,
        override val isPlaying: Boolean = false,
        override val trackData: TrackParseResult? = null,
        override val history: PersistentList<RecordPoint> = persistentListOf(),
    ) : RecordingUiState

    data class Stopped(
        override val user: UserData? = null,
        override val playerPosition: Long = 0,
        override val isPlaying: Boolean = false,
        override val trackData: TrackParseResult? = null,
        override val history: PersistentList<RecordPoint> = persistentListOf(),
    ) : RecordingUiState
}