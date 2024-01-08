package com.whatever.raisedragon.controller.betting

import com.whatever.raisedragon.applicationservice.betting.dto.BettingCreateServiceRequest
import com.whatever.raisedragon.applicationservice.betting.dto.BettingUpdateServiceRequest
import com.whatever.raisedragon.domain.betting.BettingPredictionType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "[Request] 배팅 생성")
data class BettingCreateRequest(
    @Schema(description = "Goal Id")
    val goalId: Long,

    @Schema(description = "배팅 타입 [SUCCESS, FAIL]")
    val predictionType: BettingPredictionType,
)

fun BettingCreateRequest.toServiceRequest(
    userId: Long
): BettingCreateServiceRequest = BettingCreateServiceRequest(
    userId = userId,
    goalId = goalId,
    bettingPredictionType = predictionType
)

@Schema(description = "[Request] 배팅 수정")
data class BettingUpdateRequest(
    @Schema(description = "Betting Id")
    val bettingId: Long,

    @Schema(description = "배팅 타입 [SUCCESS, FAIL]")
    val predictionType: BettingPredictionType,
)

fun BettingUpdateRequest.toServiceRequest(
    userId: Long
): BettingUpdateServiceRequest = BettingUpdateServiceRequest(
    userId = userId,
    bettingId = bettingId,
    bettingPredictionType = predictionType
)