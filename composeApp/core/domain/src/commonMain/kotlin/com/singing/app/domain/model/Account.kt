package com.singing.app.domain.model

import com.singing.app.domain.model.stable.StableInstant

data class UserData(
    val id: Int,
    val username: String,
    val avatar: String?,
)

data class UserInfo(
    val publicationsCount: Long,
    val registeredAt: StableInstant,
)