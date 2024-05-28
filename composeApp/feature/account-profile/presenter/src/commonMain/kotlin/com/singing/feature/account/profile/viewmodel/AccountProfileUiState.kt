package com.singing.feature.account.profile.viewmodel

import com.singing.app.domain.model.AccountInfo
import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.UserData

data class AccountProfileUiState(
//    val user: UserData? = null,
    val account: UserData,
    val accountInfo: DataState<AccountInfo> = DataState.Empty,
)