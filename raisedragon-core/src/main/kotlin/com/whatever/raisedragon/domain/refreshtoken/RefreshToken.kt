package com.whatever.raisedragon.domain.refreshtoken

import com.whatever.raisedragon.domain.user.User
import java.time.LocalDateTime

data class RefreshToken(
    val payload: String?,
    val user: User,
    var isDeleted: Boolean = false,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
