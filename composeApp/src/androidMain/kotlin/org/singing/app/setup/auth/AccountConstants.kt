package org.singing.app.setup.auth

object AccountConstants {
    const val ACCOUNT_TYPE = "UserAccount"
    const val ACCOUNT_TOKEN_TYPE = "UserToken"

    const val TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 3L
    const val TOKEN_REFRESH_RATE = 1000 * 60 * 60 * 24L
}