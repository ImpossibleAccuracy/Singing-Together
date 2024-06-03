package com.singing.feature.auth.domain

import com.singing.app.domain.repository.AuthRepository
import com.singing.app.domain.model.AuthResult

class RegistrationUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(username: String, password: String): AuthResult =
        authRepository.registration(username, password)
}
