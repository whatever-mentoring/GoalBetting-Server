package com.whatever.raisedragon.applicationservice.user.dto

import com.whatever.raisedragon.domain.user.Nickname
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "[Response] 유저 조회")
data class UserRetrieveResponse(
    @Schema(description = "User Id")
    val userId: Long,

    @Schema(description = "닉네임")
    val nickname: Nickname,

    @Schema(description = "닉네임 변경 이력")
    val nicknameIsModified: Boolean
)

@Schema(description = "[Response] 유저 닉네임 중복체크")
data class UserNicknameDuplicatedResponse(
    @Schema(description = "닉네임 중복 여부")
    val nicknameIsDuplicated: Boolean
)