package com.whatever.raisedragon.controller.goalcheering

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

@Schema(description = "[Request] 응원 생성")
data class CreateGoalCheeringRequest(
    @Schema(description = "다짐 id")
    val goalId: Long,

    @Schema(description = "응원 메시지")
    @field:NotNull
    val cheeringMessage: String
)

@Schema(description = "[Request] 응원 수정")
data class UpdateGoalCheeringRequest(
    @Schema(description = "응원 id")
    val cheeringId: Long,

    @Schema(description = "응원 메시지")
    @field:NotNull
    val cheeringMessage: String
)

@Schema(description = "[Request] 단건 응원 조회")
data class GoalCheeringResponse(
    @Schema(description = "응원 id")
    val id: Long,

    @Schema(description = "user id")
    val userId: Long,

    @Schema(description = "Goal id")
    val goalId: Long,

    @Schema(description = "응원 메시지")
    val cheeringMessage: String
) {
    companion object {
        fun sample(): GoalCheeringResponse = GoalCheeringResponse(
            id = 1L,
            userId = 1L,
            goalId = 1L,
            cheeringMessage = "Sample cheering message"
        )
    }
}