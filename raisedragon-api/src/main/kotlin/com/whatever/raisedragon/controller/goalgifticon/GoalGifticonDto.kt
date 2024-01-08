package com.whatever.raisedragon.controller.goalgifticon

import com.whatever.raisedragon.applicationservice.goalgifticon.dto.GoalGifticonCreateServiceRequest
import com.whatever.raisedragon.applicationservice.goalgifticon.dto.GoalGifticonUpdateServiceRequest
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

@Schema(description = "[Request] 다짐에 기프티콘 업로드")
data class GoalGifticonCreateRequest(
    @Schema(description = "기프티콘을 등록할 다짐의 Id")
    @field:NotNull
    val goalId: Long,

    @Schema(description = "기프티콘 URL")
    @field:NotNull
    val gifticonURL: String
)

fun GoalGifticonCreateRequest.toServiceRequest(
    userId: Long
): GoalGifticonCreateServiceRequest = GoalGifticonCreateServiceRequest(
    userId = userId,
    goalId = goalId,
    uploadedURL = gifticonURL
)

@Schema(description = "[Request] 다짐 기프티콘 수정")
data class GoalGifticonRequest(
    @Schema(description = "기프티콘을 등록할 다짐의 Id")
    @field:NotNull
    val goalId: Long,

    @Schema(description = "기프티콘 URL")
    @field:NotNull
    val gifticonURL: String
)

fun GoalGifticonRequest.toServiceRequest(
    userId: Long
): GoalGifticonUpdateServiceRequest = GoalGifticonUpdateServiceRequest(
    userId = userId,
    goalId = goalId,
    gifticonURL = gifticonURL
)