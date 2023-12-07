package com.whatever.raisedragon.domain.auth.jwt

data class JwtToken(
    val accessToken: String,
    val refreshToken: String
)