package com.singing.app.data.provider

import com.singing.app.domain.model.UserData
import com.singing.app.domain.provider.UserProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserProviderImpl : UserProvider {
    private val _userFlow = MutableStateFlow<UserData?>(null)
    override val userFlow: StateFlow<UserData?> = _userFlow.asStateFlow()

    override fun setCurrentUser(userData: UserData) {
        _userFlow.value = userData
    }

    override fun clear() {
        _userFlow.value = null
    }
}
