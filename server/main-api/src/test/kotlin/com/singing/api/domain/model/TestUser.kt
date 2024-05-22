package com.singing.api.domain.model

data class TestUser(
    val token: String,
    val account: AccountEntity,
)