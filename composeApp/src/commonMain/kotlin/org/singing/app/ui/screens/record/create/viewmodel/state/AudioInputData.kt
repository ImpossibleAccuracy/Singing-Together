package org.singing.app.ui.screens.record.create.viewmodel.state

import org.singing.app.ui.screens.record.create.viewmodel.model.RecordItem

data class AudioInputData(
    val isAnyInputEnabled: Boolean = true,

    val firstInput: RecordItem? = null,
    val secondInput: RecordItem? = null,
)
