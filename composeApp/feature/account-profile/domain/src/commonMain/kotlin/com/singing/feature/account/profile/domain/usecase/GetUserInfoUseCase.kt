package com.singing.feature.account.profile.domain.usecase

import com.singing.app.domain.repository.AccountRepository

class GetUserInfoUseCase(
    private val accountRepository: AccountRepository,
) {
    suspend operator fun invoke(accountId: Int) =
        accountRepository.getAccountInfo(accountId)
}