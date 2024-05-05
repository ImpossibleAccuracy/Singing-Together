package com.singing.api.security.builder.service.sample

import com.singing.api.domain.repository.AccountRepository
import com.singing.api.security.builder.service.SecurityService
import com.singing.api.service.token.TokenUtils
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DefaultSecurityServiceConfig {
    @Bean
    @ConditionalOnMissingBean(SecurityService::class)
    fun defaultSecurityService(
        accountRepository: AccountRepository,
        tokenUtils: TokenUtils
    ): SecurityService =
        DefaultSecurityService(
            accountRepository,
            tokenUtils,
        )
}
