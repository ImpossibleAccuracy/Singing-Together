package com.singing.app.common.views.model.state

data class RecordUiData(
    val accuracy: Int?,
    val filename: String?,
    val createdAt: String,
    val duration: String,
    val isSavedRemote: Boolean,
    val isPublished: Boolean,
)