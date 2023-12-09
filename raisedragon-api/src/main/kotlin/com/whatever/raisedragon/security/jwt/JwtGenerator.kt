package com.whatever.raisedragon.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtGenerator {
    internal fun generateAccessToken(
        signedKey: Key,
        claims: Claims,
        accessTokenExpiredAt: Date
    ): String {
        return Jwts.builder()
            .signWith(signedKey)
            .setHeaderParam("type", "JWT")
            .setClaims(claims)
            .setExpiration(accessTokenExpiredAt)
            .compact()
    }

    internal fun generateRefreshToken(
        signedKey: Key,
        refreshTokenExpireIn: Date
    ): String {
        return Jwts.builder()
            .signWith(signedKey)
            .setHeaderParam("type", "JWT")
            .setExpiration(refreshTokenExpireIn)
            .compact()
    }
}