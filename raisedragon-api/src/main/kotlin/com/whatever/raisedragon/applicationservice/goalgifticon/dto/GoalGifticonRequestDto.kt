package com.whatever.raisedragon.applicationservice.goalgifticon.dto

data class GoalGifticonCreateServiceRequest(
    val userId: Long,
    val goalId: Long,
    val uploadedURL: String
)

data class GoalGifticonUpdateServiceRequest(
    val userId: Long,
    val goalId: Long,
    val gifticonURL: String
)