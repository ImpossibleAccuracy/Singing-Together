package com.singing.api.security

import com.singing.api.security.builder.authentication.AccountAuthentication
import com.singing.api.security.exception.SecurityException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder

internal fun <T> throwAccessDenied(message: String? = null): T {
    throw SecurityException(message ?: "Unauthorized")
}

internal fun getAuthentication(): AccountAuthentication? =
    SecurityContextHolder.getContext().authentication as? AccountAuthentication

internal fun requireAuthentication(): AccountAuthentication =
    getAuthentication() ?: throwAccessDenied("Authorization required")

internal fun hasAnyAuthority(
    authorities: Collection<GrantedAuthority>,
    prefix: String,
    vararg strings: String
): Boolean =
    authorities.asSequence()
        .map { it.authority }
        .filterNotNull()
        .filter { it.startsWith(prefix) }
        .map { it.substring(prefix.length) }
        .filter { it.isNotEmpty() }
        .any { s ->
            strings.any {
                it.equals(s, true)
            }
        }
