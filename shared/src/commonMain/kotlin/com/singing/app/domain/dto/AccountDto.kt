package com.singing.app.domain.dto

import com.fasterxml.jackson.annotation.JsonProperty

class AccountDto(
    @JsonProperty("i")
    var id: Int? = null,

    @JsonProperty("u")
    var username: String? = null,

    @JsonProperty("a")
    var avatar: String? = null,
)
