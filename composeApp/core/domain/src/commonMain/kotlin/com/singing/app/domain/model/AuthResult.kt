package com.singing.app.domain.model

sealed interface AuthResult {
    data class Success(val user: UserData) : AuthResult

    data object NotFound : AuthResult

    data object UserAlreadyExists : AuthResult

    data object PasswordMismatch : AuthResult
}