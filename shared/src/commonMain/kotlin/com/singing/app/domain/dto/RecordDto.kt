package com.singing.app.domain.dto

import java.time.Instant

class RecordDto(
    var id: Int? = null,
    var createdAt: Instant? = null,
    var duration: Long? = null,
    var creatorId: Int? = null,
    var isPublished: Boolean? = null,

    var accuracy: Double? = null,
    var name: String? = null,
)
