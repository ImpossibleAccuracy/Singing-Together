package com.singing.domain.payload.dto

import com.fasterxml.jackson.annotation.JsonProperty

class RecordDto(
    @JsonProperty("i")
    var id: Int? = null,

    @JsonProperty("c")
    var createdAt: String? = null,

    @JsonProperty("t")
    var title: String? = null,

    @JsonProperty("a")
    var accuracy: Double? = null,

    @JsonProperty("d")
    var duration: Long? = null,

    @JsonProperty("r")
    var creatorId: Int? = null,

    @JsonProperty("p")
    var isPublished: Boolean? = null,
)
