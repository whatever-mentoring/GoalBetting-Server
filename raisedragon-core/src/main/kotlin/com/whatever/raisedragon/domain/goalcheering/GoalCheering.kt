package com.whatever.raisedragon.domain.goalcheering

import java.time.LocalDateTime

data class GoalCheering(
    val id: Long,
    val userId: Long,
    val cheeringMessage: CheeringMessage,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val deletedAt: LocalDateTime?
)

fun GoalCheeringEntity.toDTO(): GoalCheering = GoalCheering(
    id = id,
    userId = user.id,
    cheeringMessage = cheeringMessage,
    createdAt = createdAt,
    updatedAt = updatedAt,
    deletedAt = deletedAt
)