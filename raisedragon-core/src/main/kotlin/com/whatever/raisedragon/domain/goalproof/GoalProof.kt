package com.whatever.raisedragon.domain.goalproof

import com.whatever.raisedragon.domain.goal.Goal
import com.whatever.raisedragon.domain.user.User
import java.time.LocalDateTime

data class GoalProof(
    val id: Long,
    val userId: Long,
    val goalId: Long,
    val document: Document,
    var deletedAt: LocalDateTime?,
    var createdAt: LocalDateTime?,
    var updatedAt: LocalDateTime?
)

fun GoalProofEntity.toDto(): GoalProof = GoalProof(
    id = id,
    userId = userEntity.id,
    goalId = goalEntity.id,
    document = document,
    deletedAt = deletedAt,
    createdAt = createdAt,
    updatedAt = updatedAt
)