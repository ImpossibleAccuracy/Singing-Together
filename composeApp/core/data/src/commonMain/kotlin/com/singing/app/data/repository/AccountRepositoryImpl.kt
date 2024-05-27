package com.singing.app.data.repository

import com.singing.app.domain.model.AccountInfo
import com.singing.app.domain.model.UserData
import com.singing.app.domain.repository.AccountRepository

class AccountRepositoryImpl : AccountRepository {
    override suspend fun findAccount(accountId: Int): UserData? = TODO()

    override suspend fun getAccountInfo(accountId: Int): AccountInfo = TODO()
}
