package org.singing.app.setup.auth

expect class AuthStore() {
    val isLoggedIn: Boolean

    fun getUserData(): UserData

    fun getUserCredentials(): UserCredentials

    fun saveUser(credentials: UserCredentials, data: UserData): Boolean

    fun logout(): Boolean
}
