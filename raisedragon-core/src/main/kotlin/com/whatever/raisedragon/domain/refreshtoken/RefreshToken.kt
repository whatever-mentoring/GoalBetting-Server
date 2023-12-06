package com.whatever.raisedragon.domain.refreshtoken

import java.time.LocalDateTime

data class RefreshToken(
    val id: Long,
    val payload: String?,
    val userId: Long,
    var deletedAt: LocalDateTime?,
    var createdAt: LocalDateTime?,
    var updatedAt: LocalDateTime?
)

fun RefreshTokenEntity.toDto(): RefreshToken = RefreshToken(
    id = id,
    payload = payload,
    userId = userEntity.id,
    deletedAt = deletedAt,
    createdAt = createdAt,
    updatedAt = updatedAt
)
