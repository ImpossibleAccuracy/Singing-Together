package com.singing.api.security.scope

import com.singing.api.security.throwAccessDenied
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class SecurityScope : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    fun accessDenied(message: String? = null) =
        throwAccessDenied<Unit>(message)

    abstract fun hasAnyRole(vararg roles: String): Boolean

    abstract fun hasAnyPrivilege(vararg privileges: String): Boolean
}
