package com.singing.api.security.builder.filter

import com.singing.api.security.builder.authentication.AccountAuthentication
import com.singing.api.security.builder.authentication.buildAuthorities
import com.singing.api.security.builder.service.SecurityService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.coroutines.runBlocking
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

class JwtAuthenticationFilter(
    private val securityService: SecurityService,
) : OncePerRequestFilter() {
    companion object {
        const val AUTH_HEADER_NAME = "Authorization"
        const val JWT_TOKEN_PREFIX = "Bearer "
    }

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (isAlreadyLogged) {
            filterChain.doFilter(request, response)
            return
        }

        val authHeader = request.getHeader(AUTH_HEADER_NAME)

        if (authHeader == null || authHeader.isBlank() || !authHeader.startsWith(JWT_TOKEN_PREFIX)) {
            filterChain.doFilter(request, response)
            return
        }

        val jwtAuthToken = authHeader.substring(JWT_TOKEN_PREFIX.length)

        val account = runBlocking {
            securityService.authUserByRequestToken(jwtAuthToken)
        }

        if (account != null) {
            val authToken = AccountAuthentication(
                account = account,
                authorities = buildAuthorities(account),
                credentials = null,
                details = WebAuthenticationDetailsSource().buildDetails(request)
            )

            SecurityContextHolder.getContext().authentication = authToken
        }

        filterChain.doFilter(request, response)
    }

    private val isAlreadyLogged: Boolean
        get() {
            val authentication = SecurityContextHolder.getContext().authentication
            return authentication != null && authentication.isAuthenticated
        }
}
