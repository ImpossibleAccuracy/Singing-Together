package com.singing.app.domain.model

import com.singing.app.domain.model.RecordData.Cover
import com.singing.app.domain.model.stable.StableInstant
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed interface RecordData {
    val duration: Long
    val createdAt: StableInstant
    val isSavedRemote: Boolean
    val isPublished: Boolean
    val creatorId: Int

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


@OptIn(ExperimentalContracts::class)
fun RecordData.isTwoLineRecord(): Boolean {
    contract {
        returns(true) implies (this@isTwoLineRecord is Cover)
    }

    return this is Cover
}
