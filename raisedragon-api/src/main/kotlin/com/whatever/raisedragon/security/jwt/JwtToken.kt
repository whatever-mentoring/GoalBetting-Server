package com.whatever.raisedragon.security.jwt

data class JwtToken(
    val accessToken: String,
    val refreshToken: String
)