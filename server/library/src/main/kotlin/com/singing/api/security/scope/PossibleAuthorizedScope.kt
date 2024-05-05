package com.singing.api.security.scope

import com.singing.api.security.builder.authentication.AccountAuthentication
import com.singing.api.security.hasAnyAuthority

class PossibleAuthorizedScope(
    private val authentication: AccountAuthentication?
) : SecurityScope() {
    val account = authentication?.account

    val isAuthorized: Boolean
        get() = account != null

    override fun hasAnyRole(vararg roles: String): Boolean =
        authentication?.let {
            hasAnyAuthority(
                it.authorities,
                AccountAuthentication.ROLE_PREFIX,
                *roles
            )
        } ?: false

    override fun hasAnyPrivilege(vararg privileges: String): Boolean =
        authentication?.let {
            hasAnyAuthority(
                it.authorities,
                AccountAuthentication.PRIVILEGE_PREFIX,
                *privileges
            )
        } ?: false
}
