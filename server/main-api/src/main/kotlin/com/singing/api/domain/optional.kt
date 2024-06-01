package com.singing.api.domain

import com.singing.api.domain.exception.ResourceNotFoundException
import java.util.Optional

inline fun <reified T> Optional<T>.require(): T =
    orElseThrow {
        ResourceNotFoundException("${T::class.simpleName} not found")
    }

fun <T> Optional<T>.require(
    message: String
): T = orElseThrow {
    ResourceNotFoundException(message)
}
