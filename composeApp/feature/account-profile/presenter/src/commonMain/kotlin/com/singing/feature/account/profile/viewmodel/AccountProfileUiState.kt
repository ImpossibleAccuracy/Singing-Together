package com.singing.feature.account.profile.viewmodel

import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.UserData
import com.singing.app.domain.model.UserInfo

data class AccountProfileUiState(
    val user: UserData? = null,
    val account: UserData,
    val userInfo: DataState<UserInfo> = DataState.Empty,
)