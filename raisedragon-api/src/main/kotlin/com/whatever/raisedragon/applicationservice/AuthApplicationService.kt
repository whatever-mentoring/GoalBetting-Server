package com.whatever.raisedragon.applicationservice

import com.whatever.raisedragon.domain.auth.AuthService
import com.whatever.raisedragon.domain.user.Nickname
import com.whatever.raisedragon.domain.user.User
import com.whatever.raisedragon.domain.user.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AuthApplicationService(
    private val authService: AuthService,
    private val userService: UserService,
) {

    fun kakoLogin(code: String): User {
        val kakaoId = authService.getAccessToken(code)
        val user = kakaoId?.let { userService.loadByOAuthPayload(it) } ?: return userService.create(
            User(
                oauthTokenPayload = kakaoId,
                fcmTokenPayload = null,
                nickname = Nickname.generateRandomNickname()
            )
        )
        return user
    }
}