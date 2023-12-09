package com.whatever.raisedragon.domain.goalproof

import com.whatever.raisedragon.domain.gifticon.URL
import java.time.LocalDateTime

data class GoalProof(
    val id: Long,
    val userId: Long,
    val goalId: Long,
    val url: URL,
    val comment: Comment,
    var deletedAt: LocalDateTime?,
    var createdAt: LocalDateTime?,
    var updatedAt: LocalDateTime?
)

fun GoalProofEntity.toDto(): GoalProof = GoalProof(
    id = id,
    userId = userEntity.id,
    goalId = goalEntity.id,
    url = url,
    comment = comment,
    deletedAt = deletedAt,
    createdAt = createdAt,
    updatedAt = updatedAt
)