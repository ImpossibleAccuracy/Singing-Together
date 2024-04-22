package org.singing.app.setup.auth.store

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import org.singing.app.setup.auth.AccountConstants
import org.singing.app.setup.auth.UserCredentials
import org.singing.app.setup.auth.UserData
import java.util.*

class AccountHelper(context: Context) {
    companion object {
        private const val ACCOUNT_ID_KEY = "AccountId"
    }

    private val manager: AccountManager = AccountManager.get(context)

    private val tokenPreferences = AccountSharedPreferences.get(context)


    fun isLoggedIn(): Boolean =
        manager
            .getAccountsByType(AccountConstants.ACCOUNT_TYPE)
            .isNotEmpty()

    fun getAccount(): UserData? {
        val account = findInternalPrimaryAccount() ?: return null

        val token = getInternalAccountToken(account) ?: return null

        val id = manager.getUserData(account, ACCOUNT_ID_KEY).toLong()

        return UserData(
            id,
            account.name,
            token,
        )
    }

    fun getCredentials(): UserCredentials? {
        val account = findInternalPrimaryAccount() ?: return null

        val password = manager.getPassword(account) ?: return null

        return UserCredentials(
            username = account.name,
            password = password,
        )
    }

    fun saveAccount(id: Long, username: String, password: String, token: String): Account {
        val account = Account(username, AccountConstants.ACCOUNT_TYPE)

        manager.addAccountExplicitly(
            account,
            password,
            null
        )

        updateAuthToken(token)

        manager.setUserData(account, ACCOUNT_ID_KEY, id.toString())

        return account
    }

    fun updateAuthToken(token: String?) {
        val account = findInternalPrimaryAccount() ?: return

        manager.setAuthToken(
            account,
            AccountConstants.ACCOUNT_TOKEN_TYPE,
            token
        )

        val expirationDate =
            Date(System.currentTimeMillis() + AccountConstants.TOKEN_EXPIRATION)

        tokenPreferences.setTokenExpirationDate(account, expirationDate)
    }

    fun isTokenExpired(): Boolean {
        val account = findInternalPrimaryAccount() ?: return false

        return tokenPreferences.isTokenExpired(account)
    }

    fun refreshAuthToken(): String? {
        val account = findInternalPrimaryAccount() ?: return null

        val token = manager.blockingGetAuthToken(
            account,
            AccountConstants.ACCOUNT_TOKEN_TYPE,
            true
        )

        updateAuthToken(token)

        return token
    }

    fun invalidateAuthToken() {
        val account = findInternalPrimaryAccount() ?: return
        val token =
            manager.peekAuthToken(account, AccountConstants.ACCOUNT_TOKEN_TYPE)

        manager.invalidateAuthToken(AccountConstants.ACCOUNT_TYPE, token)
    }


    private fun findInternalPrimaryAccount(): Account? {
        val accounts = manager.getAccountsByType(AccountConstants.ACCOUNT_TYPE)

        if (accounts.isEmpty()) {
            return null
        }

        return accounts.first()
    }

    private fun getInternalAccountToken(account: Account): String? {
        return manager.peekAuthToken(
            account,
            AccountConstants.ACCOUNT_TOKEN_TYPE
        )
    }
}
