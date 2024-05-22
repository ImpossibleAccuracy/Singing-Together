package com.singing.api.hooks

import com.singing.api.domain.model.TestUser
import com.singing.api.security.builder.filter.JwtAuthenticationFilter.Companion.AUTH_HEADER_NAME
import com.singing.api.security.builder.filter.JwtAuthenticationFilter.Companion.JWT_TOKEN_PREFIX
import org.springframework.test.web.servlet.MockHttpServletRequestDsl

fun MockHttpServletRequestDsl.authorize(user: TestUser) {
    header(AUTH_HEADER_NAME, JWT_TOKEN_PREFIX + user.token)
}
