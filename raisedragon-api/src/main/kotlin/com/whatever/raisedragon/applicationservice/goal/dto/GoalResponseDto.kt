package com.whatever.raisedragon.applicationservice.goal.dto

import com.whatever.raisedragon.applicationservice.betting.dto.BettingRetrieveResponse
import com.whatever.raisedragon.domain.betting.Betting
import com.whatever.raisedragon.domain.betting.BettingPredictionType
import com.whatever.raisedragon.domain.betting.BettingResult
import com.whatever.raisedragon.domain.goal.Content
import com.whatever.raisedragon.domain.goal.Goal
import com.whatever.raisedragon.domain.goal.GoalType
import com.whatever.raisedragon.domain.goal.Threshold
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "[Response] 단건 다짐(Goal) 조회")
data class GoalResponse(

    @Schema(description = "다짐 생성자 id")
    val hostUserId: Long,

    @Schema(description = "다짐 생성자 nickname")
    val hostUserNickname: String,

    @Schema(description = "다짐 id")
    val id: Long,

    @Schema(description = "다짐 타입")
    val type: GoalType,

    @Schema(description = "다짐 내용")
    val content: Content,

    @Schema(description = "다짐 인증 횟수")
    val threshold: Threshold,

    @Schema(description = "다짐 시작 시간")
    val startDate: LocalDateTime,

    @Schema(description = "다짐 마감 시간")
    val endDate: LocalDateTime,

    @Schema(description = "다짐 상태 (시작 전이거나 시작 중인 경우 PROCEEDING 사용)")
    val goalResult: com.whatever.raisedragon.domain.goal.GoalResult
) {
    companion object {
        fun of(goal: Goal, hostUserNickname: String): GoalResponse = GoalResponse(
            hostUserId = goal.userId,
            hostUserNickname = hostUserNickname,
            id = goal.id,
            type = goal.type,
            content = goal.content,
            threshold = goal.threshold,
            startDate = goal.startDate,
            endDate = goal.endDate,
            goalResult = goal.goalResult
        )
    }
}

@Schema(description = "[Response] Goal과 Betting을 같이 조회합니다")
data class GoalWithBettingResponse(
    @Schema(description = "다짐 정보")
    val goal: GoalResponse,

    @Schema(description = "내가 참가한 베팅")
    val myBetting: BettingRetrieveResponse? = null,

    @Schema(description = "다짐 성공여부")
    val isSuccess: Boolean = false,

    @Schema(description = "기프티콘에 당첨된 Winner Nickname")
    val winnerNickname: String? = null
) {
    companion object {
        fun of(
            goal: Goal,
            hostUserNickname: String,
            betting: Betting? = null,
            isSuccess: Boolean = false,
            winnerNickname: String? = null
        ): GoalWithBettingResponse = GoalWithBettingResponse(
            goal = GoalResponse.of(goal, hostUserNickname),
            myBetting = betting?.let { BettingRetrieveResponse.of(it) },
            isSuccess = isSuccess,
            winnerNickname = winnerNickname
        )
    }
}

data class GoalBettingHostResponse(
    @Schema(description = "호스트 유저 id")
    val id: Long,

    @Schema(description = "호스트 유저 닉네임")
    val nickname: String,

    @Schema(description = "다짐 생성 시각")
    val goalCreatedAt: LocalDateTime,
)

data class GoalBettingParticipantResponse(
    @Schema(description = "참가자 유저 id")
    val userId: Long,

    @Schema(description = "참가자 유저 닉네임")
    val nickname: String,

    @Schema(description = "BettingId")
    val bettingId: Long,

    @Schema(description = "예측")
    val bettingPredictionType: BettingPredictionType,

    @Schema(description = "당첨 여부")
    val bettingResult: BettingResult,

    @Schema(description = "배팅 한 시각")
    val bettingCreatedAt: LocalDateTime,
)

@Schema(description = "[Response] 다짐 배팅 참가자들 조회")
data class GoalRetrieveParticipantResponse(
    val hostUser: GoalBettingHostResponse,
    val participants: List<GoalBettingParticipantResponse>
)