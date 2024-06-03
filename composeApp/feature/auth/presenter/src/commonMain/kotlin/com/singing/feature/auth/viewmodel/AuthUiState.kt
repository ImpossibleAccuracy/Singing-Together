package com.singing.feature.auth.viewmodel

import com.singing.app.domain.model.UserData

sealed interface AuthUiState {
    data object Loading : AuthUiState

    data class EnterUsername(
        val username: String = "",
        val isBlankError: Boolean = false,
        val isTooLongError: Boolean = false,
    ) : AuthUiState

    data class PasswordConfirm(
        val user: UserData,
        val password: String = "",
        val isBlankError: Boolean = false,
        val isTooLongError: Boolean = false,
        val isPasswordMismatch: Boolean = false,
    ) : AuthUiState

    data class Registration(
        val username: String,
        val password: String = "",
        val isBlankError: Boolean = false,
        val isTooLongError: Boolean = false,
    ) : AuthUiState

    data class Success(
        val user: UserData
    ) : AuthUiState
}
