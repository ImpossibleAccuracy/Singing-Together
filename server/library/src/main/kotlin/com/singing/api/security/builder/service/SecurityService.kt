package com.singing.api.security.builder.service

import com.singing.api.domain.model.AccountEntity

interface SecurityService {
    suspend fun authUserByRequestToken(token: String): AccountEntity?
}
