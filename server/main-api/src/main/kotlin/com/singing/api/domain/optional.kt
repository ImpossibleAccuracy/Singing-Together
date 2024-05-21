package com.singing.api.domain

import com.singing.api.domain.exception.ResourceNotFoundException
import java.util.*

inline fun <reified T> Optional<T>.require() =
    orElseThrow {
        ResourceNotFoundException("${T::class.simpleName} not found")
    }

fun <T> Optional<T>.require(
    message: String
) = orElseThrow {
    ResourceNotFoundException(message)
}
