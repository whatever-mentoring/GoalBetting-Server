package com.whatever.raisedragon.controller.goal

import com.whatever.raisedragon.applicationservice.goal.dto.GoalCreateServiceRequest
import com.whatever.raisedragon.applicationservice.goal.dto.GoalDeleteServiceRequest
import com.whatever.raisedragon.applicationservice.goal.dto.GoalModifyServiceRequest
import com.whatever.raisedragon.common.aop.badwordfilter.ValidateBadWord
import com.whatever.raisedragon.domain.goal.Content
import com.whatever.raisedragon.domain.goal.GoalType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

@Schema(description = "[Request] 다짐 생성")
data class GoalCreateRequest(
    @Schema(description = "다짐 타입")
    @field:NotNull
    val type: GoalType,

    @ValidateBadWord
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

fun GoalCreateRequest.toServiceRequest(userId: Long): GoalCreateServiceRequest = GoalCreateServiceRequest(
    content = Content(content),
    goalType = type,
    startDate = startDate,
    endDate = endDate,
    userId = userId,
    gifticonUrl = gifticonUrl
)

@Schema(description = "[Request] 다짐 수정")
data class GoalModifyRequest(
    @ValidateBadWord
    @Schema(description = "다짐 내용")
    @field:NotNull
    val content: String,
)

fun GoalModifyRequest.toServiceRequest(
    userId: Long,
    goalId: Long
): GoalModifyServiceRequest = GoalModifyServiceRequest(
    userId = userId,
    goalId = goalId,
    content = Content(content)
)

@Schema(description = "[Request] 다짐 삭제")
data class GoalDeleteRequest(
    @Schema(description = "다짐 내용")
    @field:NotNull
    val goalId: Long,
)

fun GoalDeleteRequest.toServiceRequest(userId: Long): GoalDeleteServiceRequest = GoalDeleteServiceRequest(
    userId = userId,
    goalId = goalId
)