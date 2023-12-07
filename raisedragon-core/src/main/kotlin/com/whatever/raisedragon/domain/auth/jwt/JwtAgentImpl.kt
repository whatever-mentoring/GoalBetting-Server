package com.whatever.raisedragon.domain.auth.jwt

import com.whatever.raisedragon.domain.user.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtAgentImpl(
    private val jwtGenerator: JwtGenerator
) : JwtAgent {

    companion object {
        private const val BEARER_PREFIX = "Bearer "
        private const val ACCESS_TOKEN_EXPIRE_TIME = (1000 * 60 * 30).toLong()
        private const val REFRESH_TOKEN_EXPIRE_TIME = (1000 * 60 * 60 * 24 * 7).toLong()
    }

    override fun provide(user: User): JwtToken {
        val now = Date().time

        return JwtToken(
            accessToken = BEARER_PREFIX + jwtGenerator.generateAccessToken(
                claims = buildClaims(user),
                accessTokenExpiredAt = Date(now + ACCESS_TOKEN_EXPIRE_TIME)
            ),
            refreshToken = BEARER_PREFIX + jwtGenerator.generateRefreshToken(
                Date(now + REFRESH_TOKEN_EXPIRE_TIME)
            )
        )
    }

    private fun buildClaims(user: User): Claims {
        val claims = Jwts.claims()
        claims.setSubject(user.nickname.value)
        claims["id"] = user.id
        return claims
    }
}