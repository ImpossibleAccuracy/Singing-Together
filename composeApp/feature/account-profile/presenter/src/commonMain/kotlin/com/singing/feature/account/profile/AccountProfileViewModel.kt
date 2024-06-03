package com.singing.feature.account.profile

import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.singing.app.domain.model.UserData
import com.singing.app.domain.provider.UserProvider
import com.singing.feature.account.profile.domain.usecase.GetAccountPublicationsUseCase
import com.singing.feature.account.profile.domain.usecase.GetUserInfoUseCase
import com.singing.feature.account.profile.viewmodel.AccountProfileIntent
import com.singing.feature.account.profile.viewmodel.AccountProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class AccountProfileViewModel(
    accountProfile: UserData,

    private val userProvider: UserProvider,
    getUserInfoUseCase: GetUserInfoUseCase,
    getAccountPublicationsUseCase: GetAccountPublicationsUseCase,
) : ScreenModel {
    private val _uiState = MutableStateFlow(
        AccountProfileUiState(
            account = accountProfile,
        )
    )
    val uiState = _uiState.asStateFlow()

    val publications = getAccountPublicationsUseCase(accountProfile).cachedIn(screenModelScope)

    init {
        screenModelScope.launch {
            val info = getUserInfoUseCase(accountProfile.id)

            _uiState.update {
                it.copy(userInfo = info)
            }
        }

        screenModelScope.launch {
            userProvider.userFlow.collect { user ->
                _uiState.update { it.copy(user = user) }
            }
        }
    }

    fun onIntent(intent: AccountProfileIntent) {
        screenModelScope.launch {
            when (intent) {
                AccountProfileIntent.Logout -> {
                    val state = uiState.value

                    if (state.account == state.user) {
                        userProvider.logout()
                    }
                }
            }
        }
    }
}
