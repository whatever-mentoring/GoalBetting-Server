package com.whatever.raisedragon.domain.goal

import java.time.LocalDateTime

data class Goal(
    val id: Long,
    val type: BettingType,
    val content: Content,
    val threshold: Threshold,
    val deadline: LocalDateTime,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val deletedAt: LocalDateTime?
)

fun GoalEntity.toGoal(): Goal = Goal(
    id = id,
    type = type,
    content = content,
    threshold = threshold,
    deadline = deadline,
    createdAt = createdAt,
    updatedAt = updatedAt,
    deletedAt = deletedAt
)