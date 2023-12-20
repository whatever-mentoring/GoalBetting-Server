package com.whatever.raisedragon.controller.goalgifticon

import com.whatever.raisedragon.applicationservice.GoalGifticonApplicationService
import com.whatever.raisedragon.common.Response
import com.whatever.raisedragon.security.authentication.UserInfo
import com.whatever.raisedragon.security.resolver.GetAuth
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "Goal-Gifticon", description = "Goal-Gifticon API")
@RestController
@RequestMapping("/v1/goal-gifticon")
class GoalGifticonController(
    private val goalGifticonApplicationService: GoalGifticonApplicationService
) {

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "다짐 내 기프티콘 생성 API",
        description = "기프티콘 생성",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @PostMapping
    fun create(
        @GetAuth userInfo: UserInfo,
        @Valid request: GoalGifticonCreateRequest,
    ): Response<GoalGifticonResponse> {
        return Response.success(
            goalGifticonApplicationService.createAndUploadGifticon(
                userId = userInfo.id,
                goalId = request.goalId,
                uploadedURL = request.gifticonURL
            )
        )
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "다짐 내 기프티콘 조회 API",
        description = "다짐 내 기프티콘 조회",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @GetMapping("/{goalId}")
    fun retrieve(@GetAuth userInfo: UserInfo, @PathVariable goalId: Long): Response<GoalGifticonResponse> {
        return Response.success(
            goalGifticonApplicationService.retrieveByGoalId(
                goalId = goalId,
                userId = userInfo.id,
            )
        )
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "다짐 내 기프티콘 수정 API",
        description = "다짐 내 기프티콘 수정",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @PutMapping
    fun update(
        @GetAuth userInfo: UserInfo,
        @RequestBody goalGifticonRequest: GoalGifticonRequest
    ): Response<GoalGifticonResponse> {
        return Response.success(
            goalGifticonApplicationService.updateGifticonURLByGoalId(
                userId = userInfo.id,
                goalId = goalGifticonRequest.goalId,
                gifticonURL = goalGifticonRequest.gifticonURL,
            )
        )
    }
}