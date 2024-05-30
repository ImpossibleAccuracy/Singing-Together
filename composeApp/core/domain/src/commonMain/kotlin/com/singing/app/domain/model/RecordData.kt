package com.singing.app.domain.model

import com.singing.app.domain.model.RecordData.Cover
import com.singing.app.domain.model.stable.StableInstant
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed interface RecordData {
    val key: ExtendedKey
    val createdAt: StableInstant
    val name: String?
    val duration: Long
    val isPublished: Boolean
    val creatorId: Int?

    val isSavedRemote: Boolean
        get() = key.remoteId != null

    val isSavedLocally: Boolean
        get() = key.localId != null

    data class Vocal(
        override val key: ExtendedKey,
        override val createdAt: StableInstant,
        override val name: String?,
        override val duration: Long,
        override val isPublished: Boolean,
        override val creatorId: Int?,
    ) : RecordData

    data class Cover(
        override val key: ExtendedKey,
        override val createdAt: StableInstant,
        val accuracy: Int,
        override val name: String?,
        override val duration: Long,
        override val isPublished: Boolean,
        override val creatorId: Int?,
    ) : RecordData
}


@OptIn(ExperimentalContracts::class)
fun RecordData.isTwoLineRecord(): Boolean {
    contract {
        returns(true) implies (this@isTwoLineRecord is Cover)
    }

    return this is Cover
}
