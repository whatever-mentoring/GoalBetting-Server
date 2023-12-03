package com.whatever.raisedragon.domain.refreshtoken

import com.whatever.raisedragon.domain.user.User
import java.time.LocalDateTime

data class RefreshToken(
    val payload: String?,
    val user: User,
    var deletedAt: LocalDateTime?,
    var createdAt: LocalDateTime?,
    var updatedAt: LocalDateTime?
)
