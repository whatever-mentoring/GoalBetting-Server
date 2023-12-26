package com.whatever.raisedragon.controller.goal

import com.whatever.raisedragon.controller.betting.BettingRetrieveResponse
import com.whatever.raisedragon.domain.betting.Betting
import com.whatever.raisedragon.domain.betting.PredictionType
import com.whatever.raisedragon.domain.betting.Result
import com.whatever.raisedragon.domain.goal.BettingType
import com.whatever.raisedragon.domain.goal.Content
import com.whatever.raisedragon.domain.goal.Goal
import com.whatever.raisedragon.domain.goal.Threshold
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

@Schema(description = "[Request] 다짐 생성")
data class GoalCreateRequest(
    @Schema(description = "다짐 타입")
    @field:NotNull
    val type: BettingType,

    @Schema(description = "다짐 내용")
    @field:NotNull
    val content: String,

    @Schema(description = "다짐 시작 시간")
    @field:NotNull
    val startDate: LocalDateTime,

    @Schema(description = "다짐 마감 시간")
    @field:NotNull
    val endDate: LocalDateTime,

    @Schema(description = "내기에 걸 기프티콘 URL")
    val gifticonUrl: String?
)

@Schema(description = "[Request] 다짐 수정")
data class GoalModifyRequest(
    @Schema(description = "다짐 내용")
    @field:NotNull
    val content: String,
)

@Schema(description = "[Request] 다짐 삭제")
data class GoalDeleteRequest(
    @Schema(description = "다짐 내용")
    @field:NotNull
    val goalId: Long,
)

@Schema(description = "[Response] 단건 다짐(Goal) 조회")
data class GoalResponse(

    @Schema(description = "다짐 생성자 id")
    val hostUserId: Long,

    @Schema(description = "다짐 생성자 nickname")
    val hostUserNickname: String,

    @Schema(description = "다짐 id")
    val id: Long,

    @Schema(description = "다짐 타입")
    val type: BettingType,

    @Schema(description = "다짐 내용")
    val content: Content,

    @Schema(description = "다짐 인증 횟수")
    val threshold: Threshold,

    @Schema(description = "다짐 시작 시간")
    val startDate: LocalDateTime,

    @Schema(description = "다짐 마감 시간")
    val endDate: LocalDateTime
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
            endDate = goal.endDate
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
    val isSuccess: Boolean = false
) {
    companion object {
        fun of(
            goal: Goal,
            hostUserNickname: String,
            betting: Betting? = null,
            isSuccess: Boolean = false
        ): GoalWithBettingResponse = GoalWithBettingResponse(
            goal = GoalResponse.of(goal, hostUserNickname),
            myBetting = betting?.let { BettingRetrieveResponse.of(it) },
            isSuccess = isSuccess
        )
    }
}

data class GoalBettingHost(
    @Schema(description = "호스트 유저 id")
    val id: Long,

    @Schema(description = "호스트 유저 닉네임")
    val nickname: String,

    @Schema(description = "다짐 생성 시각")
    val goalCreatedAt: LocalDateTime,
)

data class GoalBettingParticipant(
    @Schema(description = "참가자 유저 id")
    val userId: Long,

    @Schema(description = "참가자 유저 닉네임")
    val nickname: String,

    @Schema(description = "BettingId")
    val bettingId: Long,

    @Schema(description = "예측")
    val predictionType: PredictionType,

    @Schema(description = "당첨 여부")
    val result: Result,

    @Schema(description = "배팅 한 시각")
    val bettingCreatedAt: LocalDateTime,
)

@Schema(description = "[Response] 다짐 배팅 참가자들 조회")
data class GoalRetrieveParticipantResponse(
    val hostUser: GoalBettingHost,
    val participants: List<GoalBettingParticipant>
)