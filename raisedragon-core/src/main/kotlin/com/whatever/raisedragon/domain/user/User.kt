package com.whatever.raisedragon.domain.user

import java.time.LocalDateTime

data class User(
    val id: Long? = 0L,
    val oauthTokenPayload: String?,
    val fcmTokenPayload: String?,
    val nickname: Nickname,
    var deletedAt: LocalDateTime? = LocalDateTime.now(),
    var createdAt: LocalDateTime? = LocalDateTime.now(),
    var updatedAt: LocalDateTime? = LocalDateTime.now()
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