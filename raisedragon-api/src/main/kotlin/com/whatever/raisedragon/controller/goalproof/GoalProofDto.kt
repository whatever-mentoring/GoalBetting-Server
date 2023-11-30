package com.whatever.raisedragon.controller.goalproof

import com.whatever.raisedragon.applicationservice.dto.GoalProofResponse
import com.whatever.raisedragon.applicationservice.dto.UserResponse
import com.whatever.raisedragon.domain.goal.Goal
import com.whatever.raisedragon.domain.goalproof.Document
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "[Request] 인증내역 생성")
data class GoalProofCreateRequest(
    @Schema(description = "Goal Id")
    val goalId: Long,
)

@Schema(description = "[Request] 인증내역 수정")
data class GoalProofUpdateRequest(
    @Schema(description = "Goal Id")
    val goalId: Long,
)

@Schema(description = "[Response] 인증내역 생성/수정")
data class GoalProofCreateUpdateResponse(
    @Schema(description = "GoalProof")
    val goalProofResponse: GoalProofResponse
)

@Schema(description = "[Response] 인증내역 조회")
data class GoalProofRetrieveResponse(
    @Schema(description = "User")
    val user: UserResponse,

    @Schema(description = "Goal")
    val goal: Goal,

    @Schema(description = "인증 상세")
    val document: Document
)