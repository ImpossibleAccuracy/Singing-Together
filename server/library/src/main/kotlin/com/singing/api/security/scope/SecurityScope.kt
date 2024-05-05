package com.singing.api.security.scope

import com.singing.api.security.throwAccessDenied

abstract class SecurityScope {
    fun accessDenied(message: String? = null) =
        throwAccessDenied<Unit>(message)

    abstract fun hasAnyRole(vararg roles: String): Boolean

    abstract fun hasAnyPrivilege(vararg privileges: String): Boolean
}
