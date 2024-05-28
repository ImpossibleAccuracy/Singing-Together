package com.singing.feature.record.list.viewmodel

import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.model.UserData

data class RecordListUiState(
    val user: UserData? = null,
    val selectedRecord: DataState<RecordData>,
)