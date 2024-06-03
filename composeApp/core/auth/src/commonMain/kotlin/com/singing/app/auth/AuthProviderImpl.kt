package com.singing.app.auth

import com.singing.app.auth.store.AuthStoreData
import com.singing.app.auth.store.AuthStoreImpl
import com.singing.app.domain.model.AuthData
import com.singing.app.domain.model.UserData
import com.singing.app.domain.provider.UserProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthProviderImpl : UserProvider {
    private val authStore by lazy {
        AuthStoreImpl()
    }

    private val _userFlow by lazy {
        MutableStateFlow(
            authStore.authStoreData?.let {
                UserData(
                    id = it.id,
                    username = it.username,
                    avatar = it.avatar,
                )
            }
        )
    }
    override val userFlow: StateFlow<UserData?> by lazy { _userFlow.asStateFlow() }

    override val token: String?
        get() = authStore.authStoreData?.token

    override fun auth(userData: UserData, authData: AuthData) {
        authStore.authStoreData = AuthStoreData(
            id = userData.id,
            username = userData.username,
            avatar = userData.avatar,
            token = authData.token,
        )

        _userFlow.value = userData
    }

    override fun logout() {
        authStore.clear()

        _userFlow.value = null
    }
}