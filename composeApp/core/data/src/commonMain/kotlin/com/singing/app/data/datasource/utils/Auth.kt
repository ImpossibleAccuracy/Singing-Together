package com.singing.app.data.datasource.utils

import com.singing.app.data.exception.UnauthorizedException
import com.singing.app.domain.provider.UserProvider
import com.singing.app.domain.provider.isLoggedIn
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header

fun UserProvider.requireAuth() {
    if (!isLoggedIn) {
        throw UnauthorizedException()
    }
}

fun HttpRequestBuilder.authHeader(userProvider: UserProvider) {
    val token = userProvider.token

    if (token != null) {
        // TODO: extract constants
        header("Authorization", "Bearer $token")
    }
}