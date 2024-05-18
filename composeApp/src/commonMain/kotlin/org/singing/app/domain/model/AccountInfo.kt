package org.singing.app.domain.model

import org.singing.app.domain.model.stable.StableInstant

data class AccountInfo(
    val publicationsCount: Int,
    val registeredAt: StableInstant,
)
