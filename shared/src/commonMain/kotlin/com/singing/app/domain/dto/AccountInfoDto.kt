package com.singing.app.domain.dto

import java.time.Instant

class AccountInfoDto(
    var publicationsCount: Long? = null,
    var registeredAt: Instant? = null,
)
