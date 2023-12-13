package com.whatever.raisedragon.security.jwt

import com.whatever.raisedragon.domain.user.User
import com.whatever.raisedragon.security.authentication.UserInfo

interface JwtAgent {

    /**
     * 사용자 정보를 토대로 Jwt Token을 생성합니다.
     * JwtToken은 accessToken및 refreshToken을 포함합니다.
     *
     * @param user User DTO로 유저의 정보를 담고 있습니다.
     * @return JwtToken 을 반환합니다.
     */
    fun provide(user: User): JwtToken

    /**
     * 사용자의 accessToken을 입력받아 UserInfo 객체를 반환합니다.
     *
     * @param token 사용자가 발급받은 JWT
     * @return UserInfo 를 반환합니다.
     */
    fun extractUserFromToken(token: String): UserInfo?

    fun extractUserId(token: String): Any?

    fun reissueToken(refreshTokenPayload: String, user: User): JwtToken
}