package com.whatever.raisedragon.controller.auth.dto

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @NotBlank
    val code: String
)

data class LoginResponse(
    @NotBlank
    val accessToken: String
)