package com.singing.api.service.token

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service

@Service
class TokenServiceImpl(
    private val jwtUtils: TokenUtils,
) : TokenService {
    override suspend fun extractSubject(token: String): String? =
        withContext(Dispatchers.Default) {
            jwtUtils.extractSubject(token)
        }

    override suspend fun generateToken(subject: String): String =
        withContext(Dispatchers.Default) {
            jwtUtils.generateToken(subject)
        }

    override suspend fun generateToken(subject: String, extra: Map<String, Any>) =
        withContext(Dispatchers.Default) {
            jwtUtils.generateToken(subject, extra)
        }
}
