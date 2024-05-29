package com.singing.feature.recording.viewmodel

import com.singing.app.domain.model.TrackParseResult

sealed interface RecordingIntent {
    data class UpdateTrack(val track: TrackParseResult) : RecordingIntent
    data object ClearTrack : RecordingIntent

    data object StartPlaying : RecordingIntent
    data class UpdatePlayerPosition(val newPosition: Long) : RecordingIntent
    data object StopPlaying : RecordingIntent

    data object StartRecording : RecordingIntent
    data object StopRecording : RecordingIntent

    data object StopRecordCountdown : RecordingIntent
}