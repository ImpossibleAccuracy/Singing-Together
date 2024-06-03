package com.singing.app.data.repository

import com.singing.app.data.datasource.declaration.AccountDataSource
import com.singing.app.data.datasource.declaration.AuthDataSource
import com.singing.app.domain.model.AuthData
import com.singing.app.domain.model.AuthResult
import com.singing.app.domain.provider.UserProvider
import com.singing.app.domain.repository.AuthRepository
import pro.respawn.apiresult.ApiResult

class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource,
    private val accountDataSource: AccountDataSource.Remote,
    private val userProvider: UserProvider,
) : AuthRepository {
    override suspend fun login(username: String, password: String): AuthResult =
        when (val data = authDataSource.login(username, password)) {
            is ApiResult.Error -> AuthResult.PasswordMismatch

            is ApiResult.Success ->
                if (data.result == null) AuthResult.NotFound
                else storeAuthData(data.result!!)

            else -> throw IllegalStateException()
        }

    override suspend fun registration(username: String, password: String): AuthResult =
        when (val data = authDataSource.registration(username, password)) {
            is ApiResult.Error -> AuthResult.UserAlreadyExists

            is ApiResult.Success -> storeAuthData(data.result)

            else -> throw IllegalStateException()
        }

    private suspend fun storeAuthData(authData: AuthData): AuthResult {
        val user = !accountDataSource.fetchAccount(authData.id)

        userProvider.auth(
            userData = user,
            authData = authData,
        )

        return AuthResult.Success(user)
    }
}