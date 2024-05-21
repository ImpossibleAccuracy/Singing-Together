package org.singing.app.domain.model

import org.singing.app.domain.model.stable.StableInstant

sealed interface RecordData {
    val duration: Long
    val createdAt: StableInstant
    val isSavedRemote: Boolean
    val isPublished: Boolean
    val creatorId: Int

    val isTwoLineRecord: Boolean
        get() {
            return this is Cover
        }

    data class Vocal(
        override val duration: Long,
        override val createdAt: StableInstant,
        override val isSavedRemote: Boolean,
        override val isPublished: Boolean,
        override val creatorId: Int,
    ) : RecordData

    data class Cover(
        val accuracy: Int,
        val name: String,
        override val duration: Long,
        override val createdAt: StableInstant,
        override val isSavedRemote: Boolean,
        override val isPublished: Boolean,
        override val creatorId: Int,
    ) : RecordData
}
