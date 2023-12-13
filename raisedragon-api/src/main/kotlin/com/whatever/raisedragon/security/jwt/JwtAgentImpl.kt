package com.whatever.raisedragon.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode
import com.whatever.raisedragon.domain.refreshtoken.RefreshTokenService
import com.whatever.raisedragon.domain.user.User
import com.whatever.raisedragon.security.authentication.UserInfo
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtAgentImpl(
    private val jwtGenerator: JwtGenerator,
    private val objectMapper: ObjectMapper,
    private val refreshTokenService: RefreshTokenService,
    @Value("\${jwt.secret-key}") private val key: String
) : JwtAgent {

    private val jwtParser: JwtParser = Jwts.parserBuilder().setSigningKey(getSigningKey()).build()

    override fun provide(user: User): JwtToken {
        val now = Date().time

        return JwtToken(
            accessToken = BEARER_PREFIX + jwtGenerator.generateAccessToken(
                claims = buildClaims(user),
                signedKey = getSigningKey(),
                accessTokenExpiredAt = Date(now + ACCESS_TOKEN_EXPIRE_TIME)
            ),
            refreshToken = BEARER_PREFIX + jwtGenerator.generateRefreshToken(
                signedKey = getSigningKey(),
                refreshTokenExpireIn = Date(now + REFRESH_TOKEN_EXPIRE_TIME)
            )
        )
    }

    override fun extractUserFromToken(token: String): UserInfo {
        return jwtParser.parseClaimsJws(token).body?.let {
            objectMapper.convertValue(it[CLAIM_INFO_KEY], UserInfo::class.java)
        } ?: throw BaseException.of(ExceptionCode.E401_UNAUTHORIZED)
    }

    override fun extractUserId(token: String): Any? {
        val jwtParser = Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
        return jwtParser.parseClaimsJws(token).body["id"]
    }

    override fun reissueToken(refreshTokenPayload: String, user: User): JwtToken {
        isNotExpired(refreshTokenPayload)
        isExistRefreshToken(refreshTokenPayload)
        return provide(user)
    }

    private fun isNotExpired(refreshTokenPayload: String) {
        try {
            jwtParser.parseClaimsJws(refreshTokenPayload)
        } catch (expiredJwtException: ExpiredJwtException) {
            throw BaseException.of(
                exceptionCode = ExceptionCode.E401_UNAUTHORIZED,
                executionMessage = "토큰을 갱신하는 중, 만료된 토큰을 요청하셨습니다. 다시 로그인해주세요."
            )
        }
    }

    private fun isExistRefreshToken(refreshTokenPayload: String) {
        refreshTokenService.loadByPayload(refreshTokenPayload)
            ?: throw BaseException.of(
                exceptionCode = ExceptionCode.E400_BAD_REQUEST,
                executionMessage = "토큰을 갱신하는 중, 유효하지 않은 토큰으로 요청하셨습니다."
            )
    }

    private fun getSigningKey(): Key {
        val keyBytes = Decoders.BASE64.decode(key)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    private fun buildClaims(user: User): Claims {
        val claims = Jwts.claims()
        claims["id"] = user.id
        claims["nickname"] = user.nickname
        // claims[CLAIM_INFO_KEY] = UserInfo.from(user)
        return claims
    }

    companion object {
        const val BEARER_PREFIX = "Bearer "
        private const val ACCESS_TOKEN_EXPIRE_TIME = (1000 * 60 * 30).toLong()
        private const val REFRESH_TOKEN_EXPIRE_TIME = (1000 * 60 * 60 * 24 * 7).toLong()
        private const val CLAIM_INFO_KEY = "claim-info"
    }
}