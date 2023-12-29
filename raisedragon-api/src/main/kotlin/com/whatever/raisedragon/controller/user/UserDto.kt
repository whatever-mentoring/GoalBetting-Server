package com.whatever.raisedragon.controller.user

import com.whatever.raisedragon.common.aop.badwordfilter.ValidateBadWord
import com.whatever.raisedragon.domain.user.Nickname
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "[Request] 유저 생성")
data class UserCreateRequest(
    @Schema(description = "OAuth 토큰")
    val oauthTokenPayload: String,

    @Schema(description = "FCM 토큰")
    val fcmTokenPayload: String,
)

@Schema(description = "[Request] 유저 닉네임 수정")
data class UserNicknameUpdateRequest(
    @field:ValidateBadWord
    @Schema(description = "User Nickname")
    val nickname: String
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

    @Schema(description = "닉네임 변경 이력")
    val nicknameIsModified: Boolean
)

@Schema(description = "[Response] 유저 로그인")
data class UserLoginResponse(
    @Schema(description = "Access Token")
    val accessToken: String,

    @Schema(description = "Refresh Token")
    val refreshToken: String,
)

@Schema(description = "[Response] 유저 닉네임 중복체크")
data class UserNicknameDuplicatedRequest(
    @Schema(description = "닉네임")
    val nickname: String,
)

@Schema(description = "[Response] 유저 닉네임 중복체크")
data class UserNicknameDuplicatedResponse(
    @Schema(description = "닉네임 중복 여부")
    val nicknameIsDuplicated: Boolean
)