package com.singing.api.security.scope

import com.singing.api.security.builder.authentication.AccountAuthentication
import com.singing.api.security.hasAnyAuthority

class AuthorizedScope(
    private val authentication: AccountAuthentication
) : SecurityScope() {
    val account = authentication.account

    override fun hasAnyRole(vararg roles: String): Boolean =
        hasAnyAuthority(
            authentication.authorities,
            AccountAuthentication.ROLE_PREFIX,
            *roles
        )

    override fun hasAnyPrivilege(vararg privileges: String): Boolean =
        hasAnyAuthority(
            authentication.authorities,
            AccountAuthentication.PRIVILEGE_PREFIX,
            *privileges
        )
}
