package com.whatever.raisedragon.controller.goalgifticon

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "[Request] 다짐에 Gifticon 등록")
data class RegisterGoalGifticonRequest(
    @Schema(description = "Gifticon id")
    val gifticonId: Long
)