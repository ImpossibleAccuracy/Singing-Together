package com.singing.feature.record.viewmodel

import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.model.UserData

data class RecordDetailUiState(
    val record: DataState<RecordData>,
    val user: UserData? = null,
)