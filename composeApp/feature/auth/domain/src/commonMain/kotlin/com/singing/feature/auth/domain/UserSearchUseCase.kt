package com.singing.feature.auth.domain

import com.singing.app.domain.model.UserData
import com.singing.app.domain.repository.AccountRepository

class UserSearchUseCase(
    private val accountRepository: AccountRepository,
) {
    suspend operator fun invoke(username: String): UserData? =
        accountRepository.findAccount(username)
}