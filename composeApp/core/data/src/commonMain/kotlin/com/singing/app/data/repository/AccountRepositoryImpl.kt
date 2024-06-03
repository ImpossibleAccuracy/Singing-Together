package com.singing.app.data.repository

import com.singing.app.data.datasource.declaration.AccountDataSource
import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.UserData
import com.singing.app.domain.model.UserInfo
import com.singing.app.domain.repository.AccountRepository
import pro.respawn.apiresult.ApiResult

class AccountRepositoryImpl(
    private val dataSource: AccountDataSource.Remote,
) : AccountRepository {
    override suspend fun findAccount(accountId: Int): UserData? =
        when (val result = dataSource.fetchAccount(accountId)) {
            is ApiResult.Success -> result.result
            else -> null
        }

    override suspend fun findAccount(username: String): UserData? =
        when (val result = dataSource.fetchAccount(username)) {
            is ApiResult.Success -> result.result
            else -> null
        }

    override suspend fun getAccountInfo(accountId: Int): DataState<UserInfo> =
        dataSource.fetchAccountInfo(accountId).asDataState
}
