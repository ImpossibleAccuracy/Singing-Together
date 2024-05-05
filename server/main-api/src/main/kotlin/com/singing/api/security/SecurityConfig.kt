package com.singing.api.security

import com.singing.api.security.builder.applyJwtSecurity
import com.singing.api.security.builder.service.SecurityService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig(
    private val securityService: SecurityService
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors {
                it.disable()
            }
            .csrf {
                it.disable()
            }
            .authorizeHttpRequests {
                it.anyRequest().permitAll()
            }
            .applyJwtSecurity(securityService)

        return http.build()
    }
}
