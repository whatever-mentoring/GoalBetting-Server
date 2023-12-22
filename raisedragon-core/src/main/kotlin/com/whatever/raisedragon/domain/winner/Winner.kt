package com.whatever.raisedragon.domain.winner

import java.time.LocalDateTime

data class Winner(
    val id: Long,
    val goalId: Long,
    val userId: Long,
    val gifticonId: Long,
    var deletedAt: LocalDateTime?,
    var createdAt: LocalDateTime,
    var updatedAt: LocalDateTime
)

fun WinnerEntity.toDto(): Winner = Winner(
    id = id,
    goalId = goalEntity.id,
    userId = userEntity.id,
    gifticonId = gifticonEntity.id,
    deletedAt = deletedAt,
    createdAt = createdAt,
    updatedAt = updatedAt
)