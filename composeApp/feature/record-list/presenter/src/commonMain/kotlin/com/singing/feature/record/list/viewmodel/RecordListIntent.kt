package com.singing.feature.record.list.viewmodel

import com.singing.app.domain.model.RecordData

sealed interface RecordListIntent {
    data object ReloadRecords : RecordListIntent

    data class UpdateSelected(val record: RecordData) : RecordListIntent

    data class UploadRecord(val record: RecordData) : RecordListIntent

    data class PublishRecord(val record: RecordData, val description: String) : RecordListIntent

    data class DeleteRecord(val record: RecordData) : RecordListIntent
}