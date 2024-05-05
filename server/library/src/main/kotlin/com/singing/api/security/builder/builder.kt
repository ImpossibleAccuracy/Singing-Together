package com.singing.api.security.builder

import com.singing.api.security.builder.filter.JwtAuthenticationFilter
import com.singing.api.security.builder.service.SecurityService
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

fun HttpSecurity.applyJwtSecurity(
    securityService: SecurityService,
) {
    sessionManagement {
        it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    addFilterBefore(
        JwtAuthenticationFilter(securityService),
        UsernamePasswordAuthenticationFilter::class.java
    )
}
