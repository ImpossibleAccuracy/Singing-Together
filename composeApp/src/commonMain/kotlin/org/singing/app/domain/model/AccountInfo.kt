package org.singing.app.domain.model

import kotlinx.datetime.Instant

data class AccountInfo(
    val publicationsCount: Int,
    val registeredAt: Instant,
)
