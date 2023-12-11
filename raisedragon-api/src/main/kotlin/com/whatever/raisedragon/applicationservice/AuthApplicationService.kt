package com.whatever.raisedragon.applicationservice

import com.whatever.raisedragon.controller.auth.LoginResponse
import com.whatever.raisedragon.domain.auth.AuthService
import com.whatever.raisedragon.domain.user.Nickname
import com.whatever.raisedragon.domain.user.User
import com.whatever.raisedragon.domain.user.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthApplicationService(
    private val authService: AuthService,
    private val userService: UserService,
) {

    @Transactional
    fun kakoLogin(accessToken: String): LoginResponse {
        val kakaoId = authService.verifyKaKao(accessToken)
        return LoginResponse(
            userService.loadByOAuthPayload(kakaoId) ?: userService.create(
                    User(
                        oauthTokenPayload = kakaoId,
                        fcmTokenPayload = null,
                        nickname = Nickname.generateRandomNickname()
                    )
                )
        )
    }
}