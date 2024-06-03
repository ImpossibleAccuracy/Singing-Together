package com.singing.app.domain.repository

import com.singing.app.domain.model.AuthResult

interface AuthRepository {
    suspend fun login(username: String, password: String): AuthResult

    suspend fun registration(username: String, password: String): AuthResult
}