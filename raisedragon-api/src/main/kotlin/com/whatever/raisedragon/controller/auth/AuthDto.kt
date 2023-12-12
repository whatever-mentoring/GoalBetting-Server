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
    val userId: Long,
    val nickname: String,
    val accessToken: String,
    val refreshToken: String
)

@Schema(description = "[Response] 토큰 갱신 응답")
data class TokenRefreshResponse(
    val accessToken: String,
    val refreshToken: String
)