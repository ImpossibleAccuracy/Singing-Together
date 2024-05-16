package org.singing.app.ui.screens.record.create.viewmodel.model

import androidx.compose.runtime.Immutable

@Immutable
data class RecordItem(
    val note: String,
    val frequency: Double,
    val time: Long,
)
