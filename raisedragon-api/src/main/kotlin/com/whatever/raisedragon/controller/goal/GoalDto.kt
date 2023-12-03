package com.whatever.raisedragon.controller.goal

import com.whatever.raisedragon.domain.goal.BettingType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PositiveOrZero
import java.time.LocalDateTime

@Schema(description = "[Request] 다짐 생성")
data class GoalCreateRequest(
    @Schema(description = "다짐 타입")
    @field:NotNull
    val type: BettingType,

    @Schema(description = "다짐 내용")
    @field:NotNull
    val content: String,

    @Schema(description = "다짐 인증 횟수")
    @field:NotNull
    @field:PositiveOrZero
    val threshold: Int,

    @Schema(description = "다짐 마감 시간")
    @field:NotNull
    val deadline: LocalDateTime,
)

@Schema(description = "[Response] 단건 다짐(Goal) 조회")
data class GoalResponse(
    @Schema(description = "다짐 id")
    val id: Long,

    @Schema(description = "다짐 타입")
    val type: BettingType,

    @Schema(description = "다짐 내용")
    val content: String,

    @Schema(description = "다짐 인증 횟수")
    val threshold: Int,

    @Schema(description = "다짐 마감 시간")
    val deadline: LocalDateTime
) {
    companion object {
        fun sample(): GoalResponse = GoalResponse(
            id = 1L,
            type = BettingType.BILLING,
            content = "Sample Goal's content",
            threshold = 4,
            deadline = LocalDateTime.now().plusDays(1L)
        )
    }
}