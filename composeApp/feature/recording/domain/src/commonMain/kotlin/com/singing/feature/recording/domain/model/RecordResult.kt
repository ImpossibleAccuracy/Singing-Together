package com.singing.feature.recording.domain.model

import androidx.compose.runtime.Stable

@Stable
data class RecordResult(
    val bytes: ByteArray,
    val duration: Long,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RecordResult

        if (!bytes.contentEquals(other.bytes)) return false
        if (duration != other.duration) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bytes.contentHashCode()
        result = 31 * result + duration.hashCode()
        return result
    }
}