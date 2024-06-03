package com.singing.app.data.datasource.impl

import com.singing.app.data.datasource.declaration.AuthDataSource
import com.singing.app.data.datasource.impl.api.ApiScheme
import com.singing.app.domain.model.AuthData
import com.singing.domain.payload.request.AuthRequest
import com.singing.domain.payload.response.AuthResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import pro.respawn.apiresult.ApiResult

class AuthDataSourceImpl(
    private val httpClient: HttpClient,
) : AuthDataSource {
    override suspend fun login(username: String, password: String): ApiResult<AuthData?> = ApiResult {
        val response = httpClient.post(ApiScheme.Auth.SignIn) {
            contentType(ContentType.Application.Json)

            setBody(
                AuthRequest(
                    username = username,
                    password = password,
                )
            )
        }

        if (response.status == HttpStatusCode.NotFound) null
        else response.body<AuthResponse>().let {
            AuthData(
                id = it.id!!,
                token = it.token!!,
            )
        }
    }

    override suspend fun registration(username: String, password: String): ApiResult<AuthData> = ApiResult {
        httpClient
            .post(ApiScheme.Auth.SignUp) {
                contentType(ContentType.Application.Json)

                setBody(
                    AuthRequest(
                        username = username,
                        password = password,
                    )
                )
            }
            .body<AuthResponse>()
            .let {
                AuthData(
                    id = it.id!!,
                    token = it.token!!,
                )
            }
    }
}
