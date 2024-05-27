package com.singing.app.domain.model

import com.singing.app.domain.model.stable.StableInstant
import kotlinx.collections.immutable.ImmutableList

data class Publication(
    val author: UserData,
    val createdAt: StableInstant,
    val description: String,
    val record: RecordData,
    val tags: ImmutableList<PublicationTag>,
)

data class PublicationTag(
    val title: String
)