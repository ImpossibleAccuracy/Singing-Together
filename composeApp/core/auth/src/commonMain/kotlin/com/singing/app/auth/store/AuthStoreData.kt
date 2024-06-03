package com.singing.app.auth.store

import kotlinx.serialization.Serializable

@Serializable
data class AuthStoreData(
    val id: Int,
    val username: String,
    val avatar: String?,
    val token: String,
)
