package org.singing.app.setup.auth

import org.singing.app.MyApplication
import org.singing.app.setup.auth.store.AccountHelper
import org.singing.app.setup.auth.UserCredentials
import org.singing.app.setup.auth.UserData

actual class AuthStore {
    private val accountHelper by lazy {
        AccountHelper(MyApplication.appContext)
    }

    actual val isLoggedIn: Boolean
        get() = accountHelper.isLoggedIn()

    actual fun getUserData(): UserData =
        accountHelper.getAccount()!!

    actual fun getUserCredentials(): UserCredentials =
        accountHelper.getCredentials()!!

    actual fun saveUser(
        credentials: UserCredentials,
        data: UserData
    ): Boolean {
        accountHelper.saveAccount(
            id = data.id,
            username = data.username,
            password = credentials.password,
            token = data.token,
        )

        return true
    }

    actual fun logout(): Boolean {
        accountHelper.invalidateAuthToken()

        return true
    }
}
