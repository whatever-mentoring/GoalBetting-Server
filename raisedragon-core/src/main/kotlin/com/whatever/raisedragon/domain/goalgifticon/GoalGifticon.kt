package com.whatever.raisedragon.domain.goalgifticon

import java.time.LocalDateTime

data class GoalGifticon(
    val id: Long,
    val goalId: Long,
    val gifticonId: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val deletedAt: LocalDateTime?
)

fun GoalGifticonEntity.toDto(): GoalGifticon = GoalGifticon(
    id = id,
    goalId = goalEntity.id,
    gifticonId = gifticon.id,
    createdAt = createdAt,
    updatedAt = updatedAt,
    deletedAt = deletedAt
)