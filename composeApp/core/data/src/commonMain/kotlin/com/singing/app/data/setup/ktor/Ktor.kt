package com.singing.app.data.setup.ktor

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*

fun setupKtorClient() = HttpClient {
    install(ContentNegotiation) {
        jackson()
    }
}
