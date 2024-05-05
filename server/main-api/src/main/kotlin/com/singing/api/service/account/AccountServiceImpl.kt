package com.singing.api.service.account

import com.singing.api.domain.model.Account
import com.singing.api.domain.repository.AccountRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class AccountServiceImpl(
    private val accountRepository: AccountRepository,
) : AccountService {
    override suspend fun getByEmail(username: String): Optional<Account> =
        Optional.ofNullable(accountRepository.findByUsernameIgnoreCase(username))

    override suspend fun save(account: Account): Account =
        accountRepository.save(account)
}
