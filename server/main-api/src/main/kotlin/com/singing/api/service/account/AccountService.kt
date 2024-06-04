package com.singing.api.service.account

import com.singing.api.domain.model.AccountEntity
import com.singing.api.domain.model.DocumentEntity
import java.util.*

interface AccountService {
    suspend fun get(id: Int): Optional<AccountEntity>

    suspend fun getAvatar(id: Int): Optional<DocumentEntity>

    suspend fun getPublicationsCount(id: Int): Long

    suspend fun getByUsername(username: String): Optional<AccountEntity>

    suspend fun save(account: AccountEntity): AccountEntity
}
