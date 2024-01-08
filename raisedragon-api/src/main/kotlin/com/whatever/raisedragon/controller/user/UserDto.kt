package com.whatever.raisedragon.controller.user

import com.whatever.raisedragon.applicationservice.user.dto.UserNicknameDuplicatedServiceRequest
import com.whatever.raisedragon.applicationservice.user.dto.UserNicknameUpdateServiceRequest
import com.whatever.raisedragon.common.aop.badwordfilter.ValidateBadWord
import io.swagger.v3.oas.annotations.media.Schema


@Schema(description = "[Request] 유저 닉네임 수정")
data class UserNicknameUpdateRequest(
    @field:ValidateBadWord
    @Schema(description = "User Nickname")
    val nickname: String
)

fun UserNicknameUpdateRequest.toServiceRequest(userId: Long): UserNicknameUpdateServiceRequest =
    UserNicknameUpdateServiceRequest(userId = userId, nickname = nickname)

@Schema(description = "[Response] 유저 닉네임 중복체크")
data class UserNicknameDuplicatedRequest(
    @Schema(description = "닉네임")
    val nickname: String,
)

fun UserNicknameDuplicatedRequest.toServiceRequest(): UserNicknameDuplicatedServiceRequest =
    UserNicknameDuplicatedServiceRequest(nickname = nickname)