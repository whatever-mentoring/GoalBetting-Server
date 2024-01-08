package com.whatever.raisedragon.domain.betting

import java.time.LocalDateTime

data class Betting(
    val id: Long,
    val userId: Long,
    val goalId: Long,
    val bettingPredictionType: BettingPredictionType,
    val bettingResult: BettingResult,
    var deletedAt: LocalDateTime?,
    var createdAt: LocalDateTime?,
    var updatedAt: LocalDateTime?
)

fun BettingEntity.toDto(): Betting = Betting(
    id = id,
    userId = userEntity.id,
    goalId = goalEntity.id,
    bettingPredictionType = bettingPredictionType,
    bettingResult = bettingResult,
    deletedAt = deletedAt,
    createdAt = createdAt,
    updatedAt = updatedAt
)