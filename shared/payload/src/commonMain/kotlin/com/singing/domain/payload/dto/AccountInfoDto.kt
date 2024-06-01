package com.singing.domain.payload.dto

import com.fasterxml.jackson.annotation.JsonProperty

class AccountInfoDto(
    @JsonProperty("p")
    var publicationsCount: Long? = null,

    @JsonProperty("r")
    var registeredAt: String? = null,
)
