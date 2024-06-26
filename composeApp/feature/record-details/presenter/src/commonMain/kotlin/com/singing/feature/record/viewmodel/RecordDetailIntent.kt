package com.singing.feature.record.viewmodel

sealed interface RecordDetailIntent {
    data object UploadRecord : RecordDetailIntent

    data class PublishRecord(
        val description: String,
        val tags: List<String>,
    ) : RecordDetailIntent

    data object DeleteRecord : RecordDetailIntent
}