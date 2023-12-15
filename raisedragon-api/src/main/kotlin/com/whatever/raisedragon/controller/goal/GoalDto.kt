package com.whatever.raisedragon.controller.goal

import com.whatever.raisedragon.domain.betting.Betting
import com.whatever.raisedragon.domain.betting.PredictionType
import com.whatever.raisedragon.domain.betting.Result
import com.whatever.raisedragon.domain.goal.BettingType
import com.whatever.raisedragon.domain.goal.Content
import com.whatever.raisedragon.domain.goal.Goal
import com.whatever.raisedragon.domain.goal.Threshold
import com.whatever.raisedragon.domain.user.User
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
        fun of(goal: Goal): GoalResponse = GoalResponse(
            id = goal.id,
            type = goal.type,
            content = goal.content,
            threshold = goal.threshold,
            startDate = goal.startDate,
            endDate = goal.endDate
        )
    }
}

@Schema(description = "[Response] 다짐 배팅 참가자들 조회")
data class GoalRetrieveParticipantResponse(
    @Schema(description = "GoalId")
    val id: Long,

    @Schema(description = "UserId")
    val userId: Long,

    @Schema(description = "Nickname")
    val nickname: String,

    @Schema(description = "BettingId")
    val bettingId: Long,

    @Schema(description = "예측")
    val predictionType: PredictionType,

    @Schema(description = "당첨 여부")
    val result: Result,

    @Schema(description = "배팅 한 시각")
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(
            goal: Goal,
            betting: Betting,
            user: User,
        ): GoalRetrieveParticipantResponse =
            GoalRetrieveParticipantResponse(
                id = goal.id,
                userId = user.id!!,
                nickname = user.nickname.value,
                bettingId = betting.goalId,
                predictionType = betting.predictionType,
                result = betting.result,
                createdAt = betting.createdAt!!
            )
    }
}