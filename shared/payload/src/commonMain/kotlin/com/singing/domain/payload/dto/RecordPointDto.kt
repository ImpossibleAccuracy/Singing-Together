package com.singing.domain.payload.dto

import com.fasterxml.jackson.annotation.JsonProperty

class RecordPointDto(
    @JsonProperty("t")
    var time: Long? = null,

    @JsonProperty("f")
    var first: Double? = null,

    @JsonProperty("s")
    var second: Double? = null,
)
