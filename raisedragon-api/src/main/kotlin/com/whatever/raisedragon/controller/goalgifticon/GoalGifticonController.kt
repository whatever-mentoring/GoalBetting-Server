package com.whatever.raisedragon.controller.goalgifticon

import com.whatever.raisedragon.applicationservice.GoalGifticonApplicationService
import com.whatever.raisedragon.common.Response
import com.whatever.raisedragon.common.aop.Auth
import com.whatever.raisedragon.common.aop.AuthContext
import io.swagger.v3.oas.annotations.Operation
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

    @Auth
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "다짐 내 기프티콘 생성 API", description = "기프티콘 생성")
    @PostMapping
    fun create(
        // @GetAuth userInfo: UserInfo,
        @Valid request: GoalGifticonCreateRequest,
    ): Response<GoalGifticonResponse> {
        AuthContext.getUser()
        return Response.success(
            goalGifticonApplicationService.createAndUploadGifticon(
                userId = AuthContext.getUser().id!!,
                goalId = request.goalId,
                uploadedURL = request.gifticonURL
            )
        )
    }

    @Auth
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "다짐 내 기프티콘 조회 API", description = "다짐 내 기프티콘 조회")
    @GetMapping("/{goalId}")
    fun retrieve(@PathVariable goalId: Long): Response<GoalGifticonResponse> {
        AuthContext.getUser()
        return Response.success(
            goalGifticonApplicationService.retrieveByGoalId(
                goalId = goalId,
                userId = AuthContext.getUser().id!!,
            )
        )
    }

    @Auth
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "다짐 내 기프티콘 수정 API", description = "다짐 내 기프티콘 수정")
    @PutMapping
    fun update(
        @RequestBody goalGifticonRequest: GoalGifticonRequest
    ): Response<GoalGifticonResponse> {
        AuthContext.getUser()
        return Response.success(
            goalGifticonApplicationService.updateGifticonURLByGoalId(
                userId = AuthContext.getUser().id!!,
                goalId = goalGifticonRequest.goalId,
                gifticonURL = goalGifticonRequest.gifticonURL,
            )
        )
    }
}