package com.singing.api.service.account

import com.singing.api.domain.model.AccountEntity
import java.util.*

interface AccountService {
    suspend fun get(id: Int): Optional<AccountEntity>

    suspend fun getPublicationsCount(id: Int): Long

    suspend fun getByEmail(username: String): Optional<AccountEntity>

    suspend fun save(account: AccountEntity): AccountEntity
}
