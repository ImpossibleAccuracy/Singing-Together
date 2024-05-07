package org.singing.app.ui.screens.record.viewmodel.state

import org.singing.app.ui.screens.record.viewmodel.model.RecordItem

data class RecordData(
    val isAnyInputEnabled: Boolean = true,

    val firstInput: RecordItem? = null,
    val secondInput: RecordItem? = null,
)
