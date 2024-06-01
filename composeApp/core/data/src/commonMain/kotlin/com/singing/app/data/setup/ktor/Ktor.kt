package com.singing.app.data.setup.ktor

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.jackson.jackson

data class ApiParameters(
    val baseUrl: String,
)

fun setupKtorClient(apiParameters: ApiParameters) = HttpClient {
    defaultRequest {
        url(apiParameters.baseUrl)
    }

    install(ContentNegotiation) {
        jackson()
    }
}
