@file:OptIn(ExperimentalContracts::class)
@file:Suppress("unused")

package com.singing.api.security

import com.singing.api.security.builder.authentication.AccountAuthentication
import com.singing.api.security.scope.AuthorizedScope
import com.singing.api.security.scope.PossibleAuthorizedScope
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

suspend fun <T> tryAuthenticate(block: suspend PossibleAuthorizedScope.() -> T): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    val authentication = getAuthentication()

    return PossibleAuthorizedScope(authentication).block()
}

suspend fun <T> requireAuthenticated(block: suspend AuthorizedScope.() -> T): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    val authentication = requireAuthentication()

    return AuthorizedScope(authentication).block()
}

suspend fun <T> requireAuthenticatedOrDefault(
    default: T,
    block: suspend AuthorizedScope.() -> T
): T {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }

    return try {
        requireAuthenticated(block)
    } catch (t: Throwable) {
        default
    }
}


suspend fun <T> requireAnyRole(vararg roles: String, block: suspend AuthorizedScope.() -> T): T {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }

    if (roles.isEmpty()) {
        throwAccessDenied<Unit>("Call denied")
    }

    val authentication = requireAuthentication()

    if (hasAnyAuthority(
            authentication.authorities,
            AccountAuthentication.ROLE_PREFIX,
            *roles
        )
    ) {
        return AuthorizedScope(authentication).block()
    }

    return throwAccessDenied("Not enough rights")
}

suspend fun <T> requireAnyPermission(vararg privileges: String, block: suspend AuthorizedScope.() -> T): T {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }

    if (privileges.isEmpty()) {
        throwAccessDenied<Unit>("Call denied")
    }

    val authentication = requireAuthentication()

    if (hasAnyAuthority(
            authentication.authorities,
            AccountAuthentication.PRIVILEGE_PREFIX,
            *privileges
        )
    ) {
        return AuthorizedScope(authentication).block()
    }

    return throwAccessDenied("Not enough rights")
}

suspend fun <T> requireAnyPermissionOrDefault(
    vararg privileges: String,
    default: T,
    block: suspend AuthorizedScope.() -> T
): T {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }

    return try {
        requireAnyPermission(*privileges) {
            block.invoke(this)
        }
    } catch (t: Throwable) {
        default
    }
}
