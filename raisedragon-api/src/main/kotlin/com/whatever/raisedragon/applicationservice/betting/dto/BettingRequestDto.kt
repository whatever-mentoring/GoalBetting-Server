package com.whatever.raisedragon.applicationservice.betting.dto

import com.whatever.raisedragon.domain.betting.BettingPredictionType

data class BettingCreateServiceRequest(
    val userId: Long,
    val goalId: Long,
    val bettingPredictionType: BettingPredictionType
)

data class BettingUpdateServiceRequest(
    val userId: Long,
    val bettingId: Long,
    val bettingPredictionType: BettingPredictionType
)