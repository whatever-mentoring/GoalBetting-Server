package com.whatever.raisedragon.domain.goalproof

import com.whatever.raisedragon.domain.BaseEntity
import com.whatever.raisedragon.domain.goal.Goal
import com.whatever.raisedragon.domain.user.User

data class GoalProof(
    val user: User,
    val goal: Goal,
    val document: Document
)