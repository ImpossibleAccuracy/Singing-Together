package com.singing.app.domain.dto

import java.time.Instant

class PublicationDto(
    var id: Int? = null,
    var createdAt: Instant? = null,
    var description: String? = null,
    var account: AccountDto? = null,
    var record: RecordDto? = null,
    var tags: List<String>? = null,
)
