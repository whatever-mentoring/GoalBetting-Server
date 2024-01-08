package com.whatever.raisedragon.applicationservice.betting.dto

import com.whatever.raisedragon.domain.betting.Betting
import com.whatever.raisedragon.domain.betting.BettingPredictionType
import com.whatever.raisedragon.domain.betting.BettingResult
import io.swagger.v3.oas.annotations.media.Schema

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
    val bettingPredictionType: BettingPredictionType,

    @Schema(description = "당첨 여부")
    val bettingResult: BettingResult,
) {
    companion object {
        fun of(betting: Betting): BettingRetrieveResponse = BettingRetrieveResponse(
            id = betting.id,
            userId = betting.userId,
            goalId = betting.goalId,
            bettingPredictionType = betting.bettingPredictionType,
            bettingResult = betting.bettingResult
        )
    }
}