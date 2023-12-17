package com.whatever.raisedragon.controller.auth

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "[Request] 카카오 로그인")
data class LoginRequest(
    @NotBlank
    @Schema(description = "카카오에서 발급받은 AccessToken")
    val accessToken: String
)

@Schema(description = "[Response] 카카오 로그인 응답")
data class LoginResponse(
    @Schema(description = "id")
    val userId: Long,

    @Schema(description = "닉네임")
    val nickname: String,

    @Schema(description = "액세스 토큰")
    val accessToken: String,

    @Schema(description = "리프레시 토큰")
    val refreshToken: String,

    @Schema(description = "닉네임 변경 이력")
    val nicknameIsModified: Boolean
)

@Schema(description = "[Response] 토큰 갱신 응답")
data class TokenRefreshResponse(
    @Schema(description = "액세스 토큰")
    val accessToken: String,

    @Schema(description = "리프레시 토큰")
    val refreshToken: String,
)