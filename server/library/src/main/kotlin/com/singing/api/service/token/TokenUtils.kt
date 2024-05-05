package com.singing.api.service.token

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import java.util.function.Function

@Service
class TokenUtils(
    private val tokenProperties: TokenProperties,
) {
    private val signInKey: Key by lazy {
        Decoders.BASE64.decode(tokenProperties.secret).let {
            Keys.hmacShaKeyFor(it)
        }
    }

    fun generateToken(subject: String, extra: Map<String, Any?> = mapOf()): String =
        Jwts.builder()
            .setClaims(extra)
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + tokenProperties.expirationTime))
            .signWith(signInKey, SignatureAlgorithm.HS256)
            .compact()

    fun extractSubject(token: String) =
        extractClaim(token) { obj -> obj.subject }

    private fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>) =
        try {
            val claims = extractAllClaims(token)
            claimsResolver.apply(claims)
        } catch (e: JwtException) {
            null
        }

    private fun extractAllClaims(token: String) =
        Jwts.parserBuilder()
            .setSigningKey(signInKey)
            .build()
            .parseClaimsJws(token)
            .body
}
