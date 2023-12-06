package com.whatever.raisedragon.domain.goal

import java.time.LocalDateTime

data class Goal(
    val id: Long,
    val type: BettingType,
    val content: Content,
    val threshold: Threshold,
    val result: Result,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val deletedAt: LocalDateTime?
)

fun GoalEntity.toDto(): Goal = Goal(
    id = id,
    type = type,
    content = content,
    threshold = threshold,
    result = result,
    startDate = startDate,
    endDate = endDate,
    createdAt = createdAt,
    updatedAt = updatedAt,
    deletedAt = deletedAt
)