package com.whatever.raisedragon.controller.goal

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