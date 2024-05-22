package com.singing.app.domain.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

class PublicationDto(
    @JsonProperty("i")
    var id: Int? = null,

    @JsonProperty("c")
    var createdAt: Instant? = null,

    @JsonProperty("d")
    var description: String? = null,

    @JsonProperty("a")
    var author: AccountDto? = null,

    @JsonProperty("r")
    var record: RecordDto? = null,

    @JsonProperty("t")
    var tags: List<String>? = null,
)
