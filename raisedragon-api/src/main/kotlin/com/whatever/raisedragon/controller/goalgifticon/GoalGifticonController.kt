package com.whatever.raisedragon.controller.goalgifticon

import com.whatever.raisedragon.applicationservice.GoalGifticonApplicationService
import com.whatever.raisedragon.common.Response
import com.whatever.raisedragon.security.authentication.UserInfo
import com.whatever.raisedragon.security.resolver.GetAuth
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Tag(name = "Goal-Gifticon", description = "Goal-Gifticon API")
@RestController
@RequestMapping("/v1/goal-gifticon")
class GoalGifticonController(
    private val goalGifticonApplicationService: GoalGifticonApplicationService
) {

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Gifticon create API", description = "Create Gifticon")
    @PostMapping(
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun create(
        @GetAuth userInfo: UserInfo,
        @Valid @RequestBody request: GoalGifticonCreateRequest,
        @RequestParam gifticonFile: MultipartFile
    ): Response<GoalGifticonResponse> {
            return Response.success(
                goalGifticonApplicationService.createAndUploadGifticon(
                    userId = userInfo.id,
                    goalId = request.goalId,
                    gifticonFile = gifticonFile
                )
            )
    }
}