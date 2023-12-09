package com.whatever.raisedragon.controller.goalproof

import com.whatever.raisedragon.domain.goalproof.Document
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "[Request] 인증내역 생성")
data class GoalProofCreateRequest(
    @Schema(description = "Goal Id")
    val goalId: Long,

    @Schema(description = "다짐 인증에 대한 PresignedURL")
    val document: String
)

@Schema(description = "[Request] 인증내역 수정")
data class GoalProofUpdateRequest(
    @Schema(description = "Goal Id")
    val goalId: Long,
)

@Schema(description = "[Response] 인증내역 생성/수정")
data class GoalProofCreateUpdateResponse(
    @Schema(description = "GoalProof")
    val goalProofRetrieveResponse: GoalProofRetrieveResponse
)

@Schema(description = "[Response] 인증내역 조회")
data class GoalProofRetrieveResponse(

    @Schema(description = "GoalProofId")
    val id: Long,

    @Schema(description = "UserId")
    val userId: Long,

    @Schema(description = "GoalId")
    val goalId: Long,

    @Schema(description = "인증 상세")
    val document: Document
)