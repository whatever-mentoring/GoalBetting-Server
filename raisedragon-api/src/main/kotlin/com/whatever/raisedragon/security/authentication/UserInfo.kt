package com.whatever.raisedragon.security.authentication

import com.whatever.raisedragon.domain.user.User

data class UserInfo(
    val id: Long,
    val nickname: String,
) {
    companion object {
        fun from(user: User): UserInfo {
            return UserInfo(
                user.id!!,
                user.nickname.value,
            )
        }
    }
}

fun UserInfo.toDetails(): Map<String, String> {
    return mapOf("nickname" to nickname)
}