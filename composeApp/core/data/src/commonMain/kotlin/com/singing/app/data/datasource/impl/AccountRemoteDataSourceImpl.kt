package com.singing.app.data.datasource.impl

import com.singing.app.data.datasource.declaration.AccountDataSource
import com.singing.app.data.datasource.impl.api.ApiScheme
import com.singing.app.data.datasource.utils.DataMapper
import com.singing.app.data.datasource.utils.authHeader
import com.singing.app.data.datasource.utils.mapBody
import com.singing.app.domain.model.UserData
import com.singing.app.domain.model.UserInfo
import com.singing.app.domain.provider.UserProvider
import com.singing.domain.payload.dto.AccountDto
import com.singing.domain.payload.dto.AccountInfoDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import pro.respawn.apiresult.ApiResult

class AccountRemoteDataSourceImpl(
    private val userProvider: UserProvider,
    private val httpClient: HttpClient,
    private val dataMapperUser: DataMapper<AccountDto, UserData>,
    private val dataMapperUserInfo: DataMapper<AccountInfoDto, UserInfo>,
) : AccountDataSource.Remote {
    override suspend fun fetchAccount(id: Int): ApiResult<UserData> = ApiResult {
        httpClient
            .get(ApiScheme.Account.Details(id)) {
                authHeader(userProvider)
            }
            .mapBody(dataMapperUser)
    }

    override suspend fun fetchAccountInfo(id: Int): ApiResult<UserInfo> = ApiResult {
        httpClient
            .get(ApiScheme.Account.Info(id)) {
                authHeader(userProvider)
            }
            .mapBody(dataMapperUserInfo)
    }
}