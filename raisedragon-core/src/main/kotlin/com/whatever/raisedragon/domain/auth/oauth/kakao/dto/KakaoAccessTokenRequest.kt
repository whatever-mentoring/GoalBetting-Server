package com.whatever.raisedragon.domain.auth.oauth.kakao.dto

data class KakaoAccessTokenRequest(
    val grant_type: String,
    val client_id: String,
    val redirect_uri: String,
    val code: String
)