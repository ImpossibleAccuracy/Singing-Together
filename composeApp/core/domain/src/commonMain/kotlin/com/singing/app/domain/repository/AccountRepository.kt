package com.singing.app.domain.repository

import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.UserData
import com.singing.app.domain.model.UserInfo

interface AccountRepository {
    suspend fun findAccount(accountId: Int): UserData?

    suspend fun getAccountInfo(accountId: Int): DataState<UserInfo>
}