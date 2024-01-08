package com.whatever.raisedragon.domain.goal

import java.time.LocalDateTime

data class Goal(
    val id: Long,
    val userId: Long,
    val type: GoalType,
    val content: Content,
    val threshold: Threshold,
    val goalResult: GoalResult,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val deletedAt: LocalDateTime?
)

fun GoalEntity.toDto(): Goal = Goal(
    id = id,
    userId = userEntity.id,
    type = goalType,
    content = content,
    threshold = threshold,
    goalResult = goalResult,
    startDate = startDate,
    endDate = endDate,
    createdAt = createdAt,
    updatedAt = updatedAt,
    deletedAt = deletedAt
)