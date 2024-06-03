package com.singing.feature.auth

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.singing.app.domain.model.AuthResult
import com.singing.feature.auth.domain.LoginUseCase
import com.singing.feature.auth.domain.RegistrationUseCase
import com.singing.feature.auth.domain.UserSearchUseCase
import com.singing.feature.auth.viewmodel.AuthIntent
import com.singing.feature.auth.viewmodel.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val MAX_USERNAME_SIZE = 50
private const val MAX_PASSWORD_SIZE = 150

class AuthViewModel(
    private val userSearchUseCase: UserSearchUseCase,
    private val loginUseCase: LoginUseCase,
    private val registrationUseCase: RegistrationUseCase,
) : ScreenModel {
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.EnterUsername())
    val uiState = _uiState.asStateFlow()

    fun onIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.UpdateLogin -> {
                _uiState.update {
                    if (it is AuthUiState.EnterUsername) {
                        it.copy(
                            username = intent.value,
                            isBlankError = false,
                            isTooLongError = false,
                        )
                    } else {
                        AuthUiState.EnterUsername(username = intent.value)
                    }
                }
            }

            AuthIntent.SubmitLogin -> onSubmitLogin()

            is AuthIntent.UpdatePassword -> {
                _uiState.update {
                    when (it) {
                        is AuthUiState.PasswordConfirm -> {
                            it.copy(
                                password = intent.value,
                                isBlankError = false,
                                isTooLongError = false,
                                isPasswordMismatch = false,
                            )
                        }

                        is AuthUiState.Registration -> {
                            it.copy(
                                password = intent.value,
                                isBlankError = false,
                                isTooLongError = false,
                            )
                        }

                        else -> throw IllegalArgumentException()
                    }
                }
            }

            AuthIntent.SubmitPassword -> {
                if (uiState.value is AuthUiState.PasswordConfirm) {
                    onSubmitPasswordSignIn()
                } else if (uiState.value is AuthUiState.Registration) {
                    onSubmitPasswordSignUp()
                }
            }
        }
    }

    private fun onSubmitLogin() {
        val state = uiState.value

        if (state !is AuthUiState.EnterUsername) throw IllegalArgumentException()

        val username = state.username.trim()

        val isBlank = username.isBlank()
        val isTooLong = username.length > MAX_USERNAME_SIZE
        val isValid = !isBlank && !isTooLong

        _uiState.update {
            state.copy(
                isBlankError = isBlank,
                isTooLongError = isTooLong,
            )
        }

        if (isValid) {
            screenModelScope.launch {
                _uiState.value = AuthUiState.Loading

                val user = userSearchUseCase(username)

                _uiState.update {
                    if (user == null) {
                        AuthUiState.Registration(username)
                    } else {
                        AuthUiState.PasswordConfirm(user)
                    }
                }
            }
        }
    }

    private fun onSubmitPasswordSignIn() {
        val state = uiState.value

        if (state !is AuthUiState.PasswordConfirm) throw IllegalArgumentException()

        val password = state.password.trim()

        val isBlank = password.isBlank()
        val isTooLong = password.length > MAX_PASSWORD_SIZE
        val isValid = !isBlank && !isTooLong

        _uiState.update {
            state.copy(
                isBlankError = isBlank,
                isTooLongError = isTooLong,
            )
        }

        if (isValid) {
            screenModelScope.launch {
                _uiState.value = AuthUiState.Loading

                val result = loginUseCase(
                    username = state.user.username,
                    password = password,
                )

                _uiState.update {
                    when (result) {
                        is AuthResult.Success -> AuthUiState.Success(result.user)
                        AuthResult.PasswordMismatch -> state.copy(isPasswordMismatch = true)
                        else -> throw IllegalStateException()
                    }
                }
            }
        }
    }

    private fun onSubmitPasswordSignUp() {
        val state = uiState.value

        if (state !is AuthUiState.Registration) throw IllegalArgumentException()

        val password = state.password.trim()

        val isBlank = password.isBlank()
        val isTooLong = password.length > MAX_PASSWORD_SIZE
        val isValid = !isBlank && !isTooLong

        _uiState.update {
            state.copy(
                isBlankError = isBlank,
                isTooLongError = isTooLong,
            )
        }

        if (isValid) {
            screenModelScope.launch {
                _uiState.value = AuthUiState.Loading

                val result = registrationUseCase(
                    username = state.username,
                    password = password,
                )

                _uiState.update {
                    when (result) {
                        is AuthResult.Success -> AuthUiState.Success(result.user)
                        else -> throw IllegalStateException()
                    }
                }
            }
        }
    }
}