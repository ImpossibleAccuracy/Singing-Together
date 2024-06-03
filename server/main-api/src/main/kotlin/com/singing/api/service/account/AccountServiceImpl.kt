package com.singing.api.service.account

import com.singing.api.domain.model.AccountEntity
import com.singing.api.domain.repository.AccountRepository
import com.singing.api.domain.repository.PublicationRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class AccountServiceImpl(
    private val accountRepository: AccountRepository,
    private val publicationRepository: PublicationRepository,
) : AccountService {
    override suspend fun get(id: Int): Optional<AccountEntity> =
        accountRepository.findById(id)

    override suspend fun getPublicationsCount(id: Int): Long =
        publicationRepository.countByAccount_Id(id)

    override suspend fun getByUsername(username: String): Optional<AccountEntity> =
        Optional.ofNullable(accountRepository.findByUsernameIgnoreCase(username))

    override suspend fun save(account: AccountEntity): AccountEntity =
        accountRepository.save(account)
}
