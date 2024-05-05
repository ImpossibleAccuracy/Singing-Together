package org.singing.app.setup.auth

import com.russhwolf.settings.contains
import org.singing.app.setup.auth.store.authSettings

actual class AuthStore {
    companion object {
        private const val ID_KEY = "id"
        private const val USERNAME_KEY = "username"
        private const val PASSWORD_KEY = "password"
        private const val TOKEN_KEY = "token"
    }

    actual val isLoggedIn: Boolean
        get() = authSettings.contains(TOKEN_KEY)

    actual fun getUserData(): UserData =
        findAccount()!!.first

    actual fun getUserCredentials(): UserCredentials =
        findAccount()!!.second

    actual fun saveUser(
        credentials: UserCredentials,
        data: UserData
    ): Boolean {
        authSettings.putLong(ID_KEY, data.id)
        authSettings.putString(USERNAME_KEY, data.username)
        authSettings.putString(PASSWORD_KEY, credentials.password)
        authSettings.putString(TOKEN_KEY, data.token)

        return true
    }

    actual fun logout(): Boolean {
        authSettings.clear()

        return true
    }

    private fun findAccount(): Pair<UserData, UserCredentials>? {
        val id = authSettings.getLongOrNull(ID_KEY) ?: return null
        val username = authSettings.getStringOrNull(USERNAME_KEY) ?: return null
        val password = authSettings.getStringOrNull(PASSWORD_KEY) ?: return null
        val token = authSettings.getStringOrNull(TOKEN_KEY) ?: return null

        return UserData(
            id,
            username,
            token,
        ) to UserCredentials(
            username = username,
            password = password,
        )
    }
}
