package com.whatever.raisedragon.controller.auth

import com.whatever.raisedragon.applicationservice.auth.dto.LoginServiceRequest
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "[Request] 카카오 로그인")
data class LoginRequest(
    @NotBlank
    @Schema(description = "카카오에서 발급받은 AccessToken")
    val accessToken: String
)

fun LoginRequest.toServiceRequest(): LoginServiceRequest = LoginServiceRequest(accessToken = accessToken)