package com.singing.app.domain.payload

import androidx.compose.runtime.Immutable
import com.singing.domain.model.AudioFile

@Immutable
data class RecordSaveData(
    val title: String?,
    val record: ByteArray,
    val track: AudioFile?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RecordSaveData

        if (!record.contentEquals(other.record)) return false
        if (track != other.track) return false

        return true
    }

    override fun hashCode(): Int {
        var result = record.contentHashCode()
        result = 31 * result + (track?.hashCode() ?: 0)
        return result
    }
}
