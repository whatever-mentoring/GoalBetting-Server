package com.whatever.raisedragon.controller.betting

import com.whatever.raisedragon.domain.betting.Betting
import com.whatever.raisedragon.domain.betting.PredictionType
import com.whatever.raisedragon.domain.betting.Result
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
    val bettingRetrieveResponse: BettingRetrieveResponse,
)

@Schema(description = "[Response] 배팅 조회")
data class BettingRetrieveResponse(
    @Schema(description = "BettingId")
    val id: Long,

    @Schema(description = "UserId")
    val userId: Long,

    @Schema(description = "GoalId")
    val goalId: Long,

    @Schema(description = "예측")
    val predictionType: PredictionType,

    @Schema(description = "당첨 여부")
    val result: Result,
) {
    companion object {
        fun of(betting: Betting): BettingRetrieveResponse = BettingRetrieveResponse(
            id = betting.id,
            userId = betting.userId,
            goalId = betting.goalId,
            predictionType = betting.predictionType,
            result = betting.result
        )
    }
}