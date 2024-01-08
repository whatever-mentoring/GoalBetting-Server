package com.whatever.raisedragon.applicationservice.goal.dto

import com.whatever.raisedragon.domain.goal.Content
import com.whatever.raisedragon.domain.goal.GoalType
import java.time.LocalDateTime

data class GoalCreateServiceRequest(
    val content: Content,
    val goalType: GoalType,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val userId: Long,
    val gifticonUrl: String? = null
)

data class GoalModifyServiceRequest(
    val userId: Long,
    val goalId: Long,
    val content: Content,
)

data class GoalDeleteServiceRequest(
    val userId: Long,
    val goalId: Long,
)