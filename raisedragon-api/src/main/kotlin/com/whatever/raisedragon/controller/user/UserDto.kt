package com.whatever.raisedragon.controller.user

import com.whatever.raisedragon.domain.user.Nickname
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "[Request] 유저 생성")
data class UserCreateRequest(
    @Schema(description = "OAuth 토큰")
    val oauthTokenPayload: String,

    @Schema(description = "FCM 토큰")
    val fcmTokenPayload: String,
)

@Schema(description = "[Request] 유저 수정")
data class UserUpdateRequest(
    @Schema(description = "User Id")
    val userId: Long
)

@Schema(description = "[Response] 유저 생성/수정")
data class UserCreateUpdateResponse(
    @Schema(description = "User")
    val userRetrieveResponse: UserRetrieveResponse
)

@Schema(description = "[Response] 유저 조회")
data class UserRetrieveResponse(
    @Schema(description = "User Id")
    val userId: Long,

    @Schema(description = "닉네임")
    val nickname: Nickname,
)