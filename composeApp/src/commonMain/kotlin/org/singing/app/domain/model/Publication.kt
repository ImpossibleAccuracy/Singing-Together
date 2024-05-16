package org.singing.app.domain.model

import kotlinx.datetime.Instant

data class Publication(
    val author: AccountUiData,
    val createdAt: Instant,
    val description: String,
    val record: RecordData,
)
