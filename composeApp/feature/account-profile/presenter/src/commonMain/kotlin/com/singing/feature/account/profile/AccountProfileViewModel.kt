package com.singing.feature.account.profile

import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.singing.app.domain.model.UserData
import com.singing.feature.account.profile.domain.usecase.GetAccountPublicationsUseCase
import com.singing.feature.account.profile.viewmodel.AccountProfileIntent
import com.singing.feature.account.profile.viewmodel.AccountProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class AccountProfileViewModel(
    accountProfile: UserData,

    getAccountPublicationsUseCase: GetAccountPublicationsUseCase,
) : ScreenModel {
    private val _uiState = MutableStateFlow(AccountProfileUiState(accountProfile))
    val uiState = _uiState.asStateFlow()

    val publications = getAccountPublicationsUseCase(accountProfile).cachedIn(screenModelScope)

    init {
        screenModelScope.launch {

        }
    }

    fun onIntent(intent: AccountProfileIntent) {
        screenModelScope.launch {
//            when (intent) {
//            }
        }
    }
}
