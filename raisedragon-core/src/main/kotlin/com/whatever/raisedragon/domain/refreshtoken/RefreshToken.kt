package com.whatever.raisedragon.domain.refreshtoken

import java.time.LocalDateTime

data class RefreshToken(
    val id: Long = 0L,
    var payload: String?,
    val userId: Long,
    var deletedAt: LocalDateTime? = null,
    var createdAt: LocalDateTime? = LocalDateTime.now(),
    var updatedAt: LocalDateTime? = LocalDateTime.now()
)

fun RefreshTokenEntity.toDto(): RefreshToken = RefreshToken(
    id = id,
    payload = payload,
    userId = userEntity.id,
    deletedAt = deletedAt,
    createdAt = createdAt,
    updatedAt = updatedAt
)
