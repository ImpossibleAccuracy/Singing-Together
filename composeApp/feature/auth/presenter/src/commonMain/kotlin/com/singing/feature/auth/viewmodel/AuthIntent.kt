package com.singing.feature.auth.viewmodel

sealed interface AuthIntent {
    data class UpdateLogin(val value: String) : AuthIntent
    data object SubmitLogin : AuthIntent

    data class UpdatePassword(val value: String) : AuthIntent
    data object SubmitPassword : AuthIntent
}