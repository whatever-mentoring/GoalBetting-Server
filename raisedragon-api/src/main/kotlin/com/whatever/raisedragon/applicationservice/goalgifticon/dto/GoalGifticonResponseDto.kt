package com.whatever.raisedragon.applicationservice.goalgifticon.dto

import io.swagger.v3.oas.annotations.media.Schema

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

@Schema(description = "[Response] 기프티콘 조회")
data class GifticonResponse(
    @Schema(description = "기프티콘을 업로드한 Goal의 Id")
    val goalId: Long,

    @Schema(description = "생성된 Gifticon Id")
    val gifticonId: Long,

    @Schema(description = "업로드된 Gifticon URL")
    val gifticonURL: String
)