package com.singing.api.security.builder.service.sample

import com.singing.api.domain.model.Account
import com.singing.api.domain.repository.AccountRepository
import com.singing.api.security.builder.service.SecurityService
import com.singing.api.service.token.TokenUtils

class DefaultSecurityService(
    private val accountRepository: AccountRepository,
    private val tokenUtils: TokenUtils,
) : SecurityService {
    override suspend fun authUserByRequestToken(token: String): Account? {
        val username = tokenUtils.extractSubject(token) ?: return null

        return accountRepository.findByUsernameIgnoreCase(username)
    }
}
