package com.whatever.raisedragon.domain.auth.oauth.kakao.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoAccessTokenResponse(
    @JsonProperty("access_token")
    val accessToken: String? = null,

    @JsonProperty("token_type")
    val tokenType: String? = null,

    @JsonProperty("refresh_token")
    val refreshToken: String? = null,

    @JsonProperty("id_token")
    val idToken: String? = null,

    @JsonProperty("expires_in")
    val expiresIn: Int = 0,

    val scope: String? = null,

    @JsonProperty("refresh_token_expires_in")
    val refreshTokenExpiresIn: Int = 0
)