package org.singing.app.setup.auth.store

import android.accounts.Account
import android.content.Context
import android.content.SharedPreferences
import java.util.Date

class AccountSharedPreferences private constructor(
    private val preferences: SharedPreferences,
) {
    companion object {
        private const val ACCOUNT_PREFERENCES_NAME = "ACCOUNT_PREFERENCES"

        fun get(context: Context): AccountSharedPreferences {
            val preferences =
                context.getSharedPreferences(ACCOUNT_PREFERENCES_NAME, Context.MODE_PRIVATE)

            return AccountSharedPreferences(preferences)
        }

        fun get(preferences: SharedPreferences): AccountSharedPreferences {
            return AccountSharedPreferences(preferences)
        }
    }

    fun isTokenExpired(account: Account): Boolean {
        val expirationDate = getTokenExpirationDate(account) ?: return true

        return expirationDate.before(Date())
    }

    fun setTokenExpirationDate(account: Account, expirationDate: Date) {
        preferences.edit().apply {
            putLong(account.name, expirationDate.time)

            apply()
        }
    }

    private fun getTokenExpirationDate(account: Account): Date? {
        val timestamp = preferences.getLong(account.name, -1)

        if (timestamp == -1L) {
            return null
        }

        return Date(timestamp)
    }
}