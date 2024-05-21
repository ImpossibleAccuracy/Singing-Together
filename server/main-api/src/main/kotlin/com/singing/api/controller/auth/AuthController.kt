package com.singing.api.controller.auth

import com.singing.api.domain.exception.OperationRejectedException
import com.singing.api.domain.exception.ResourceNotFoundException
import com.singing.api.domain.model.AccountEntity
import com.singing.api.security.requireAuthenticated
import com.singing.api.service.account.AccountService
import com.singing.api.service.token.TokenService
import com.singing.app.domain.payload.AuthRequest
import com.singing.app.domain.payload.AuthResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val accountService: AccountService,
    private val tokenService: TokenService,
    private val passwordEncoder: PasswordEncoder,
) {
    @PostMapping("/signIn")
    suspend fun signIn(
        @RequestBody body: AuthRequest,
    ): AuthResponse {
        val account = accountService
            .getByEmail(body.username)
            .orElseThrow {
                ResourceNotFoundException("Account not found")
            }

        if (!passwordEncoder.matches(body.password, account.password)) {
            throw OperationRejectedException("Password mismatch")
        }

        return AuthResponse(
            id = account.id!!,
            token = tokenService.generateToken(account.username!!)
        )
    }

    @PostMapping("/signUp")
    suspend fun signUp(
        @RequestBody body: AuthRequest,
    ): AuthResponse {
        accountService.getByEmail(body.username)
            .ifPresent {
                throw OperationRejectedException("User with such email already exists")
            }

        val account = AccountEntity(
            username = body.username,
            password = passwordEncoder.encode(body.password)
        ).let {
            accountService.save(it)
        }

        return AuthResponse(
            id = account.id!!,
            token = tokenService.generateToken(account.username!!)
        )
    }

    @PostMapping("/token")
    @SecurityRequirement(name = "bearerAuth")
    suspend fun tokenUpdate(): AuthResponse = requireAuthenticated {
        AuthResponse(
            id = account.id!!,
            token = tokenService.generateToken(account.username!!)
        )
    }
}
