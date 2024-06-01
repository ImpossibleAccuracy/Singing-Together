package com.singing.domain.payload.dto

import com.fasterxml.jackson.annotation.JsonProperty

class PublicationDto(
    @JsonProperty("i")
    var id: Int? = null,

    @JsonProperty("c")
    var createdAt: String? = null,

    @JsonProperty("d")
    var description: String? = null,

    @JsonProperty("a")
    var author: AccountDto? = null,

    @JsonProperty("r")
    var record: RecordDto? = null,

    @JsonProperty("t")
    var tags: List<String>? = null,
)
