package com.whatever.raisedragon.domain.auth.jwt

import com.whatever.raisedragon.domain.user.User

interface JwtAgent {

    /**
     * 사용자 정보를 토대로 Jwt Token을 생성합니다.
     * JwtToken은 accessToken및 refreshToken을 포함합니다.
     *
     * @param user User DTO로 유저의 정보를 담고 있습니다.
     * @return JwtToken 을 반환합니다.
     */
    fun provide(user: User): JwtToken

}