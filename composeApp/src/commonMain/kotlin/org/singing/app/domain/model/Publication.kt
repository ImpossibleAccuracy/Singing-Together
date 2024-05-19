package org.singing.app.domain.model

import kotlinx.collections.immutable.ImmutableList
import org.singing.app.domain.model.stable.StableInstant

data class Publication(
    val author: AccountUiData,
    val createdAt: StableInstant,
    val description: String,
    val record: RecordData,
    val tags: ImmutableList<PublicationTag>,
)
