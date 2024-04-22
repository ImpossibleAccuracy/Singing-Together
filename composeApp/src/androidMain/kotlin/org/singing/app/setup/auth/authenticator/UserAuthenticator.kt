package org.singing.app.setup.auth.authenticator

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf

class UserAuthenticator(
    private val context: Context,
) : AbstractAccountAuthenticator(context) {
    override fun addAccount(
        response: AccountAuthenticatorResponse?,
        accountType: String?,
        authTokenType: String?,
        requiredFeatures: Array<out String>?,
        options: Bundle?
    ): Bundle = bundleOf(
//        AccountManager.KEY_INTENT to Intent(context, LoginActivity::class.java).apply {
//            putExtra(LoginActivity.ARG_ACCOUNT_TYPE, accountType)
//            putExtra(LoginActivity.ARG_AUTH_TYPE, authTokenType)
//            putExtra(LoginActivity.ARG_IS_ADDING_NEW_ACCOUNT, true)
//            putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
//        }
    )

    override fun getAuthToken(
        response: AccountAuthenticatorResponse?,
        account: Account,
        authTokenType: String,
        options: Bundle?
    ): Bundle {
        val accountManager = AccountManager.get(context)

        val token = accountManager.peekAuthToken(account, authTokenType)
        var resultToken = token

        if (token.isNullOrBlank()) {
            val password = accountManager.getPassword(account)

            if (password != null) {
//                val loginResult = authRepository.login(account.name, password)
//
//                resultToken = loginResult.getOrNull()
            }
        } else {
//            val refreshResult = tokenRepository.refreshToken()
//
//            resultToken = refreshResult.getOrDefault(oken)
        }

        if (!resultToken.isNullOrBlank()) {
            return bundleOf(
                Pair(AccountManager.KEY_ACCOUNT_NAME, account.name),
                Pair(AccountManager.KEY_ACCOUNT_TYPE, account.type),
                Pair(AccountManager.KEY_AUTHTOKEN, resultToken)
            )
        }

//        val intent = Intent(context, LoginActivity::class.java)
//        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
//        intent.putExtra(LoginActivity.ARG_ACCOUNT_TYPE, account.type)
//        intent.putExtra(LoginActivity.ARG_AUTH_TYPE, authTokenType)

        val bundle = Bundle()
//        bundle.putParcelable(AccountManager.KEY_INTENT, intent)
        return bundle
    }

    override fun getAccountRemovalAllowed(response: AccountAuthenticatorResponse?, account: Account?): Bundle =
        bundleOf(
            AccountManager.KEY_BOOLEAN_RESULT to true
        )

    override fun getAuthTokenLabel(authTokenType: String?): String {
        return authTokenType ?: "No label"
    }

    override fun editProperties(response: AccountAuthenticatorResponse?, accountType: String?): Bundle? {
        return null
    }

    override fun confirmCredentials(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        options: Bundle?
    ): Bundle? {
        return null
    }

    override fun updateCredentials(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        authTokenType: String?,
        options: Bundle?
    ): Bundle? {
        return null
    }

    override fun hasFeatures(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        features: Array<out String>?
    ): Bundle? {
        return null
    }
}
