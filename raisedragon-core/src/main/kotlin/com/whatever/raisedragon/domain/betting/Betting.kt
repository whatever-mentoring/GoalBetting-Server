package com.whatever.raisedragon.domain.betting

import java.time.LocalDateTime

data class Betting(
    val id: Long,
    val userId: Long,
    val goalId: Long,
    val predictionType: PredictionType,
    val result: Result,
    var deletedAt: LocalDateTime?,
    var createdAt: LocalDateTime?,
    var updatedAt: LocalDateTime?
)

fun BettingEntity.toDto(): Betting = Betting(
    id = id,
    userId = userEntity.id,
    goalId = goalEntity.id,
    predictionType = predictionType,
    result = result,
    deletedAt = deletedAt,
    createdAt = createdAt,
    updatedAt = updatedAt
)