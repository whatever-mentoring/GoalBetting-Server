package com.whatever.raisedragon.domain.user

import java.time.LocalDateTime

data class User(
    val id: Long,
    val oauthTokenPayload: String?,
    val fcmTokenPayload: String?,
    val nickname: Nickname,
    var deletedAt: LocalDateTime?,
    var createdAt: LocalDateTime?,
    var updatedAt: LocalDateTime?
)

fun UserEntity.toDto(): User = User(
    id = id,
    oauthTokenPayload = oauthTokenPayload,
    fcmTokenPayload = fcmTokenPayload,
    nickname = nickname,
    createdAt = createdAt,
    updatedAt = updatedAt,
    deletedAt = deletedAt
)