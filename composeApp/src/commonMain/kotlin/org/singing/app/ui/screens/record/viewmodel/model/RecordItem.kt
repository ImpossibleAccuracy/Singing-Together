package org.singing.app.ui.screens.record.viewmodel.model

import androidx.compose.runtime.Immutable

@Immutable
data class RecordItem(
    val note: String,
    val frequency: Double,
    val time: Long,
)
