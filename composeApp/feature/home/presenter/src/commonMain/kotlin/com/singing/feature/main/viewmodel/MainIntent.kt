package com.singing.feature.main.viewmodel

import com.singing.app.domain.model.RecentTrack
import com.singing.app.domain.model.RecordData

sealed interface MainIntent {
    data class PublishRecord(
        val record: RecordData,
        val description: String,
        val tags: List<String>,
    ) : MainIntent

    data class UploadRecord(val record: RecordData) : MainIntent

    data class DeleteRecord(val record: RecordData) : MainIntent

    data class UpdateTrackFavourite(
        val track: RecentTrack,
        val isFavourite: Boolean
    ) : MainIntent
}