package com.singing.domain.payload.request

data class AuthRequest(
    val username: String,
    val password: String,
)
