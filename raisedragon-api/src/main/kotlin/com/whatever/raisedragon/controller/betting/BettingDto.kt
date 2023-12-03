package com.whatever.raisedragon.controller.betting

import com.whatever.raisedragon.applicationservice.dto.BettingResponse
import com.whatever.raisedragon.applicationservice.dto.UserResponse
import com.whatever.raisedragon.domain.betting.PredictionType
import com.whatever.raisedragon.domain.betting.Result
import com.whatever.raisedragon.domain.goal.Goal
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "[Request] 배팅 생성")
data class BettingCreateRequest(
    @Schema(description = "Goal Id")
    val goalId: Long,

    @Schema(description = "배팅 타입 [SUCCESS, FAIL]")
    val predictionType: PredictionType,
)

@Schema(description = "[Request] 배팅 수정")
data class BettingUpdateRequest(
    @Schema(description = "Betting Id")
    val bettingId: Long,

    @Schema(description = "배팅 타입 [SUCCESS, FAIL]")
    val predictionType: PredictionType,
)

@Schema(description = "[Response] 배팅 생성/수정")
data class BettingCreateUpdateResponse(
    @Schema(description = "Goal Id")
    val bettingResponse: BettingResponse,
)

@Schema(description = "[Response] 배팅 조회")
data class BettingRetrieveResponse(
    @Schema(description = "User")
    val user: UserResponse,

    @Schema(description = "Goal")
    val goal: Goal,

    @Schema(description = "예측")
    val predictionType: PredictionType,

    @Schema(description = "배팅 결과")
    val result: Result
)