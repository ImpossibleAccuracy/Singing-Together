package com.singing.domain.payload.dto

import com.fasterxml.jackson.annotation.JsonProperty

class CategoryInfoDto(
    @JsonProperty("t")
    var title: String? = null,

    @JsonProperty("p")
    var publications: Long? = null,
)