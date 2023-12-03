package com.whatever.raisedragon.domain.goalproof

import com.whatever.raisedragon.domain.goal.Goal
import com.whatever.raisedragon.domain.goal.GoalEntity
import com.whatever.raisedragon.domain.user.User
import java.time.LocalDateTime

data class GoalProof(
    val id: Long,
    val user: User,
    val goal: Goal,
    val document: Document,
    var deletedAt: LocalDateTime?,
    var createdAt: LocalDateTime?,
    var updatedAt: LocalDateTime?
)