package com.whatever.raisedragon.applicationservice

import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode
import com.whatever.raisedragon.controller.auth.LoginResponse
import com.whatever.raisedragon.controller.auth.TokenRefreshResponse
import com.whatever.raisedragon.domain.auth.AuthService
import com.whatever.raisedragon.domain.refreshtoken.RefreshToken
import com.whatever.raisedragon.domain.refreshtoken.RefreshTokenService
import com.whatever.raisedragon.domain.user.Nickname
import com.whatever.raisedragon.domain.user.User
import com.whatever.raisedragon.domain.user.UserService
import com.whatever.raisedragon.domain.user.fromDto
import com.whatever.raisedragon.security.jwt.JwtAgent
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthApplicationService(
    private val authService: AuthService,
    private val userService: UserService,
    private val refreshTokenService: RefreshTokenService,
    private val jwtAgent: JwtAgent
) {

    @Transactional
    fun kakoLogin(accessToken: String): LoginResponse {
        val kakaoId = authService.verifyKaKao(accessToken)
        val user = userService.loadByOAuthPayload(kakaoId) ?: userService.create(
            User(
                oauthTokenPayload = kakaoId,
                fcmTokenPayload = null,
                nickname = Nickname.generateRandomNickname()
            )
        )
        val jwtToken = jwtAgent.provide(user)
        val refreshToken = RefreshToken(
            userId = user.id!!,
            payload = jwtToken.refreshToken,
        )

        refreshTokenService.create(refreshToken, user.fromDto())

        return LoginResponse(
            userId = user.id!!,
            nickname = user.nickname.value,
            accessToken = jwtToken.accessToken,
            refreshToken = jwtToken.refreshToken
        )
    }

    @Transactional
    fun reissueToken(refreshToken: String): TokenRefreshResponse {
        val refreshTokenVo = refreshTokenService.loadByPayload(refreshToken) ?: throw BaseException.of(
            exceptionCode = ExceptionCode.E400_BAD_REQUEST,
            executionMessage = "잘못된 토큰으로 요청하셨습니다."
        )

        val userId = refreshTokenVo.userId
        val user = userService.loadById(userId)
        val jwtToken = jwtAgent.reissueToken(refreshToken, user)

        refreshTokenVo.payload = jwtToken.refreshToken
        refreshTokenService.updatePayloadByVo(refreshTokenVo, user.fromDto())

        return TokenRefreshResponse(
            accessToken = jwtToken.accessToken,
            refreshToken = jwtToken.refreshToken
        )
    }
}