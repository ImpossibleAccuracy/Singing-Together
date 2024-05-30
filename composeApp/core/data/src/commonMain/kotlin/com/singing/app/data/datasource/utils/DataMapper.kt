package com.singing.app.data.datasource.utils

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

interface DataMapper<T, R> {
    fun map(source: T): R
}

suspend inline fun <reified T, reified R> HttpResponse.mapBody(mapper: DataMapper<T, R>): R {
    val data = body<T>()

    return mapper.map(data)
}
