package com.whatever.raisedragon.controller.goalproof

import com.whatever.raisedragon.domain.goalproof.GoalProof
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "[Request] 다짐 인증 생성")
data class GoalProofCreateRequest(
    @Schema(description = "Goal Id")
    val goalId: Long,

    @Schema(description = "다짐 인증에 사용한 이미지 url")
    val url: String?,

    @Schema(description = "다짐 인증에 대한 부연설명")
    val comment: String
)

@Schema(description = "[Request] 다짐 인증 수정")
data class GoalProofUpdateRequest(
    @Schema(description = "다짐 인증에 사용한 이미지 url")
    val url: String? = null,

    @Schema(description = "다짐 인증에 대한 부연설명")
    val comment: String? = null
)

@Schema(description = "[Response] 인증내역 생성/수정")
data class GoalProofCreateUpdateResponse(
    @Schema(description = "GoalProof")
    val goalProofRetrieveResponse: GoalProofRetrieveResponse
)

@Schema(description = "[Response] 단건 다짐 인증 조회")
data class GoalProofRetrieveResponse(

    @Schema(description = "GoalProofId")
    val id: Long,

    @Schema(description = "UserId")
    val userId: Long,

    @Schema(description = "GoalId")
    val goalId: Long,

    @Schema(description = "인증 사진")
    val url: String,

    @Schema(description = "인증 부연설명")
    val comment: String,
) {
    companion object {
        fun of(goalProof: GoalProof): GoalProofRetrieveResponse = GoalProofRetrieveResponse(
            id = goalProof.id,
            userId = goalProof.userId,
            goalId = goalProof.goalId,
            url = goalProof.url.value,
            comment = goalProof.comment.value
        )
    }
}

@Schema(description = "[Response] 모든 다짐 인증 조회")
data class GoalProofListRetrieveResponse(
    @Schema(description = "모든 다짐 인증")
    val goalProofs: List<GoalProofRetrieveResponse>,

    @Schema(description = "인증 순서")
    val progressDays: List<Int>
)