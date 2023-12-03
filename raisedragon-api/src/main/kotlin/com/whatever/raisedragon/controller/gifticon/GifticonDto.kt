package com.whatever.raisedragon.controller.gifticon

import com.whatever.raisedragon.domain.gifticon.URL
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

@Schema(description = "[Request] 인증내역 생성")
data class GifticonCreateRequest(
    @Schema(description = "Gitficon image url")
    @field:NotNull
    val url: String
)

@Schema(description = "[Response] 단건 Gifticon 조회")
data class GifticonResponse(
    @Schema(description = "Gifticon id")
    val id: Long,

    @Schema(description = "Gifticon owner id")
    val userId: Long,

    @Schema(description = "Gifticon image url")
    val url: URL,

    @Schema(description = "Gifticon 유효 여부")
    val isValidated: Boolean,
) {
    companion object {
        fun sample(): GifticonResponse = GifticonResponse(
            id = 1L,
            userId = 1L,
            url = URL("https://smaple-gifticon.url"),
            isValidated = false
        )
    }
}