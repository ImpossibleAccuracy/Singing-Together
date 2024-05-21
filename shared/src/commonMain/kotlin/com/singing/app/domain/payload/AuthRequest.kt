package com.singing.app.domain.payload

data class AuthRequest(
    val username: String,
    val password: String,
)
