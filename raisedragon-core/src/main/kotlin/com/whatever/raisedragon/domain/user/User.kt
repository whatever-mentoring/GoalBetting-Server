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