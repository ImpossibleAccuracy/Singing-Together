package com.singing.app.domain.repository

import com.singing.app.domain.model.AccountInfo
import com.singing.app.domain.model.UserData

interface AccountRepository {
    suspend fun findAccount(accountId: Int): UserData?

    suspend fun getAccountInfo(accountId: Int): AccountInfo
}