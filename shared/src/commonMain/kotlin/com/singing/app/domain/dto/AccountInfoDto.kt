package com.singing.app.domain.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

class AccountInfoDto(
    @JsonProperty("p")
    var publicationsCount: Long? = null,

    @JsonProperty("r")
    var registeredAt: Instant? = null,
)
