package com.whatever.raisedragon.applicationservice.user.dto

data class UserNicknameUpdateServiceRequest(
    val userId: Long,
    val nickname: String
)

data class UserNicknameDuplicatedServiceRequest(
    val nickname: String
)