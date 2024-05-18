package org.singing.app.domain.model

import org.singing.app.domain.model.stable.StableInstant

data class Publication(
    val author: AccountUiData,
    val createdAt: StableInstant,
    val description: String,
    val record: RecordData,
)
