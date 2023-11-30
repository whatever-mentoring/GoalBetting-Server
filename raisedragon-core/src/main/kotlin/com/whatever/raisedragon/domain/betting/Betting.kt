package com.whatever.raisedragon.domain.betting

import com.whatever.raisedragon.domain.goal.Goal
import com.whatever.raisedragon.domain.user.User
import java.time.LocalDateTime

data class Betting(
    val id: Long,
    val user: User,
    val goal: Goal,
    val predictionType: PredictionType,
    val result: Result,
    var deletedAt: LocalDateTime?,
    var createdAt: LocalDateTime?,
    var updatedAt: LocalDateTime?
)