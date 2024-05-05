package com.singing.api.domain.pojo

import com.singing.api.domain.model.Record
import java.time.Instant

data class RecordDto(
    var id: Int? = null,

    var createdAt: Instant = Instant.now(),

    var duration: Long? = null,

    var account: Int? = null,

    var voiceRecord: Int? = null,

    var track: Int? = null,
) {
    companion object {
        fun fromModel(model: Record) =
            RecordDto(
                id = model.id,
                createdAt = model.createdAt,
                duration = model.duration,
                account = model.account?.id,
                voiceRecord = model.voiceRecord?.id,
                track = model.track?.id,
            )
    }
}
