package com.whatever.raisedragon.controller.goal

import com.whatever.raisedragon.applicationservice.GoalApplicationService
import com.whatever.raisedragon.common.Response
import com.whatever.raisedragon.common.aop.Auth
import com.whatever.raisedragon.common.aop.AuthContext
import com.whatever.raisedragon.domain.goal.Content
import com.whatever.raisedragon.security.authentication.UserInfo
import com.whatever.raisedragon.security.resolver.GetAuth
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "Goal", description = "Goal API")
@RestController
@RequestMapping("/v1/goal")
class GoalController(
    private val goalApplicationService: GoalApplicationService
) {

    @Auth
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creating Goal API", description = "다짐을 생성합니다.")
    @PostMapping
    fun create(
        @Valid @RequestBody request: GoalCreateRequest,
        // @GetAuth userinfo: UserInfo
    ): Response<GoalResponse> {
        AuthContext.getUser()
        return Response.success(
            goalApplicationService.createGoal(
                bettingType = request.type,
                content = Content(request.content),
                startDate = request.startDate,
                endDate = request.endDate,
                userId = AuthContext.getUser().id!!
            )
        )
    }

    @Auth
    @Operation(summary = "Retrieving Goal API", description = "다짐 단일 조회")
    @GetMapping("/{goalId}")
    fun retrieveOne(
        @PathVariable goalId: Long,
        // @GetAuth userInfo: UserInfo
    ): Response<GoalResponse> {
        return Response.success(goalApplicationService.retrieveGoal(goalId))
    }

    @Auth
    @Operation(summary = "Retrieving Multiple Goal API", description = "요청자의 모든 다짐 조회")
    @GetMapping
    fun retrieveMany(
        // @GetAuth userInfo: UserInfo
    ): Response<List<GoalResponse>> {
        return Response.success(
            goalApplicationService.retrieveAllByUserId(
                AuthContext.getUser().id!!
            )
        )
    }

    @Auth
    @Operation(summary = "Retrieving GoalBetting API", description = "해당 다짐에 대한 모든 배팅 조회")
    @GetMapping("/betting/{goalId}")
    fun retrieveParticipant(
        @PathVariable goalId: Long
    ): Response<GoalRetrieveParticipantResponse> {
        return Response.success(
            goalApplicationService.retrieveGoalBettingParticipant(
                userId = AuthContext.getUser().id!!,
                goalId = goalId
            )
        )
    }

    @Auth
    @Operation(summary = "Modify Goal API", description = "다짐 세부 내용 수정")
    @PutMapping("{goalId}")
    fun modifyGoal(
        // @GetAuth userInfo: UserInfo,
        @PathVariable goalId: Long,
        @RequestBody goalModifyRequest: GoalModifyRequest
    ): Response<GoalResponse> {
        return Response.success(
            goalApplicationService.modifyGoal(
                userId = AuthContext.getUser().id!!,
                goalId = goalId,
                content = goalModifyRequest.content
            )
        )
    }

    @Auth
    @Operation(summary = "Delete Goal API", description = "다짐 삭제")
    @DeleteMapping
    fun modifyGoal(
        @GetAuth userInfo: UserInfo,
        @RequestBody goalDeleteRequest: GoalDeleteRequest
    ): Response<Unit> {
        return Response.success(
            goalApplicationService.deleteGoal(
                userId = userInfo.id,
                goalId = goalDeleteRequest.goalId,
            )
        )
    }
}