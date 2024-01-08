package com.whatever.raisedragon.controller.goalproof

import com.whatever.raisedragon.applicationservice.goalproof.dto.GoalProofCreateServiceRequest
import com.whatever.raisedragon.applicationservice.goalproof.dto.GoalProofUpdateServiceRequest
import com.whatever.raisedragon.domain.goalproof.Comment
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

fun GoalProofCreateRequest.toServiceRequest(
    userId: Long
): GoalProofCreateServiceRequest = GoalProofCreateServiceRequest(
    userId = userId,
    goalId = goalId,
    url = url,
    comment = Comment(comment)
)

@Schema(description = "[Request] 다짐 인증 수정")
data class GoalProofUpdateRequest(
    @Schema(description = "다짐 인증에 사용한 이미지 url")
    val url: String? = null,

    @Schema(description = "다짐 인증에 대한 부연설명")
    val comment: String? = null
)

fun GoalProofUpdateRequest.toServiceRequest(userId: Long, goalProofId: Long): GoalProofUpdateServiceRequest =
    GoalProofUpdateServiceRequest(
        userId = userId,
        goalProofId = goalProofId,
        url = url,
        comment = comment?.let { Comment(it) }
)