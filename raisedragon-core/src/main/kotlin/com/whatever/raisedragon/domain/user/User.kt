package com.whatever.raisedragon.domain.user

import java.time.LocalDateTime

data class User(
    val id: Long = 0L,
    val oauthTokenPayload: String?,
    val fcmTokenPayload: String?,
    val nickname: String,
    var isDeleted: Boolean = false,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
)