package com.singing.feature.auth.domain

import com.singing.app.domain.repository.AuthRepository
import com.singing.app.domain.model.AuthResult

class LoginUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(username: String, password: String): AuthResult =
        authRepository.login(username, password)
}