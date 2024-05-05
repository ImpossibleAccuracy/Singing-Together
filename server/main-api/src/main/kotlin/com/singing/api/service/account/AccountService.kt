package com.singing.api.service.account

import com.singing.api.domain.model.Account
import java.util.*

interface AccountService {
    suspend fun getByEmail(username: String): Optional<Account>

    suspend fun save(account: Account): Account
}
