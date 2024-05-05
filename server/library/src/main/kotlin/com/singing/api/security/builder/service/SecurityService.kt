package com.singing.api.security.builder.service

import com.singing.api.domain.model.Account

interface SecurityService {
    suspend fun authUserByRequestToken(token: String): Account?
}
