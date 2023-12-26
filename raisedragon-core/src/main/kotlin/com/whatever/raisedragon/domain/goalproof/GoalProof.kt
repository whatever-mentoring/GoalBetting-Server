package com.whatever.raisedragon.domain.goalproof

import com.whatever.raisedragon.domain.gifticon.URL
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.abs

data class GoalProof(
    val id: Long,
    val userId: Long,
    val goalId: Long,
    val url: URL,
    val comment: Comment,
    val progressDay: Long,
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
    progressDay = abs(ChronoUnit.DAYS.between(createdAt, goalEntity.startDate)),
    deletedAt = deletedAt,
    createdAt = createdAt,
    updatedAt = updatedAt
)