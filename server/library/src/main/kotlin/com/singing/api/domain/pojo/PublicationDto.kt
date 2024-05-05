package com.singing.api.domain.pojo

import com.singing.api.domain.model.Publication
import java.time.Instant

data class PublicationDto(
    var id: Int? = null,
    var createdAt: Instant? = null,
    var description: String? = null,
    var accountId: Int? = null,
    var record: RecordDto? = null,
    var tags: List<String>? = null,
) {
    companion object {
        fun fromModel(model: Publication) =
            PublicationDto(
                id = model.id,
                createdAt = model.createdAt,
                description = model.description,
                accountId = model.account?.id,
                record = model.record?.let { RecordDto.fromModel(it) },
                tags = model.tags.map { it.title!! }
            )
    }
}
