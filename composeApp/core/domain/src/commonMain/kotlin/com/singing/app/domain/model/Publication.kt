package com.singing.app.domain.model

import com.singing.app.domain.model.stable.StableInstant
import kotlinx.collections.immutable.PersistentList

const val MAX_PUBLICATION_DESCRIPTION_LENGTH = 300

data class Publication(
    val id: Int,
    val author: UserData,
    val createdAt: StableInstant,
    val description: String,
    val record: RecordData,
    val tags: PersistentList<PublicationTag>,
)

data class PublicationTag(
    val title: String
)