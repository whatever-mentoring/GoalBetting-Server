package com.whatever.raisedragon.controller.gifticon

import com.whatever.raisedragon.common.Response
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@Tag(name = "Gifticon", description = "Gifticon API")
@RestController
@RequestMapping("/v1/gifticon")
class GifticonController {

    @Operation(summary = "Gifticon create API", description = "Create Gifticon")
    @PostMapping
    fun create(
        @Valid @RequestBody request: GifticonCreateRequest
    ): Response<GifticonResponse> {
        return Response.success(GifticonResponse.sample())
    }

    @Operation(summary = "Gifticon delete API", description = "Delete Gifticon")
    @DeleteMapping("/{gifticonId}")
    fun delete(@PathVariable gifticonId: Long): Response<Unit> {
        // TODO: To get user id for using jwt
        return Response.success()
    }
}