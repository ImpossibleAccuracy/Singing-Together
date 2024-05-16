package org.singing.app.domain.model

import kotlinx.datetime.Instant

sealed interface RecordData {
    val duration: Long
    val createdAt: Instant
    val isSavedRemote: Boolean
    val isPublished: Boolean

    data class Vocal(
        override val duration: Long,
        override val createdAt: Instant,
        override val isSavedRemote: Boolean,
        override val isPublished: Boolean,
    ) : RecordData

    data class Cover(
        val accuracy: Int,
        val filename: String,
        override val duration: Long,
        override val createdAt: Instant,
        override val isSavedRemote: Boolean,
        override val isPublished: Boolean,
    ) : RecordData
}