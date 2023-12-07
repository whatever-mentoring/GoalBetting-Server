package com.whatever.raisedragon.domain.auth

interface AuthService {
    /**
     * 사용자가 전달한 토큰을 가지고 사용자의 카카오 유저 아이디를 반환하는 메소드입니다.
     *
     * @param userToken 사용자가 클라이언트에서 발급받은 토큰입니다.
     * @return 사용자의 카카오 유저 아이디. 반환 값이 없거나 유효하지 않은 토큰인 경우 null을 반환합니다.
     */
    fun verifyKaKao(userToken: String): String
}