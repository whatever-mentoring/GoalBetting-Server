package com.whatever.raisedragon.domain.auth.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtGenerator {

    @Value("\${jwt.secret-key}")
    private var key: String? = null

    internal fun generateAccessToken(
        claims: Claims,
        accessTokenExpiredAt: Date
    ): String {
        return Jwts.builder()
            .signWith(getSigningKey())
            .setHeaderParam("type", "JWT")
            .setClaims(claims)
            .setExpiration(accessTokenExpiredAt)
            .compact()
    }

    internal fun generateRefreshToken(
        refreshTokenExpireIn: Date
    ): String {
        return Jwts.builder()
            .signWith(getSigningKey())
            .setHeaderParam("type", "JWT")
            .setExpiration(refreshTokenExpireIn)
            .compact()
    }

    private fun getSigningKey(): Key {
        val keyBytes = Decoders.BASE64.decode(key)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}