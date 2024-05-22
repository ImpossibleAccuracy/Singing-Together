package com.singing.api.setup

import com.singing.api.domain.store.TestUserStore
import com.singing.api.security.builder.service.SecurityService
import io.mockk.coEvery

object MockAuth {
    fun setupAuth(securityService: SecurityService) {
        TestUserStore.users.forEach {
            coEvery { securityService.authUserByRequestToken(it.token) } returns it.account
        }
    }
}
