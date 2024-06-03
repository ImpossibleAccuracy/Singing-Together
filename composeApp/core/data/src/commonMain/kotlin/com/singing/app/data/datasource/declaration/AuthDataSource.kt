package com.singing.app.data.datasource.declaration

import com.singing.app.domain.model.AuthData
import pro.respawn.apiresult.ApiResult

interface AuthDataSource {
    suspend fun login(username: String, password: String): ApiResult<AuthData?>

    suspend fun registration(username: String, password: String): ApiResult<AuthData>
}