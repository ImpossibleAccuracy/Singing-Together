package com.singing.app.domain.provider

import com.singing.app.domain.model.AuthData
import com.singing.app.domain.model.UserData
import kotlinx.coroutines.flow.StateFlow

interface UserProvider {
    val userFlow: StateFlow<UserData?>

    val token: String?

    fun auth(userData: UserData, authData: AuthData)

    fun logout()
}

val UserProvider.currentUser: UserData?
    get() = userFlow.value

inline val UserProvider.isLoggedIn: Boolean
    get() = currentUser != null
