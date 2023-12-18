package com.whatever.raisedragon.controller.goalgifticon

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

@Schema(description = "[Response] 다짐에 기프티콘 업로드")
data class GoalGifticonResponse(
    @Schema(description = "생성된 Goal-Gifticon Id")
    val goalGifticonId: Long,

    @Schema(description = "기프티콘을 업로드한 Goal의 Id")
    val goalId: Long,

    @Schema(description = "생성된 Gifticon Id")
    val gifticonId: Long,

    @Schema(description = "업로드된 Gifticon URL")
    val gifticonURL: String
)