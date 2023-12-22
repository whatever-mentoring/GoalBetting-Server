package com.whatever.raisedragon.controller.me

import com.whatever.raisedragon.applicationservice.GoalApplicationService
import com.whatever.raisedragon.common.Response
import com.whatever.raisedragon.controller.goal.GoalResponse
import com.whatever.raisedragon.controller.goal.GoalWithBettingResponse
import com.whatever.raisedragon.security.authentication.UserInfo
import com.whatever.raisedragon.security.resolver.GetAuth
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Me", description = "Me API")
@RestController
@RequestMapping("/v1/me")
class MeController(
    private val goalApplicationService: GoalApplicationService
) {

    @Operation(
        summary = "Retrieving Multiple Goal API",
        description = "요청자의 모든 다짐 조회",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @GetMapping("/goal")
    fun retrieveMany(
        @GetAuth userInfo: UserInfo
    ): Response<List<GoalResponse>> {
        return Response.success(goalApplicationService.retrieveAllByUserId(userInfo.id))
    }

    @Operation(
        summary = "Retrieving my bet Goals API",
        description = "요청자가 참여한 모든 내기 조회",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @GetMapping("/bet-goal")
    fun retrieveMyBetGoals(@GetAuth userInfo: UserInfo): Response<List<GoalWithBettingResponse>> {
        return Response.success(goalApplicationService.retrieveGoalWithBettingByBetUserId(userInfo.id))
    }
}