package org.singing.app.ui.screens.record.state

import org.singing.app.ui.screens.record.model.RecordItem
import org.singing.app.ui.screens.record.model.RecordPair

data class RecordData(
    val isAnyInputEnabled: Boolean = true,

    val firstInput: RecordItem? = null,
    val secondInput: RecordItem? = null,

    val recordStartedAt: Long = 0,
    val history: List<RecordPair> = listOf(),
)
