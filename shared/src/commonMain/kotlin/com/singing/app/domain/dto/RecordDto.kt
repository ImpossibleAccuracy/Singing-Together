package com.singing.app.domain.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

class RecordDto(
    @JsonProperty("i")
    var id: Int? = null,

    @JsonProperty("c")
    var createdAt: Instant? = null,

    @JsonProperty("t")
    var title: String? = null,

    @JsonProperty("a")
    var accuracy: Double? = null,

    @JsonProperty("d")
    var duration: Long? = null,

    @JsonProperty("r")
    var creatorId: Int? = null,

    @JsonProperty("i")
    var isPublished: Boolean? = null,
)
