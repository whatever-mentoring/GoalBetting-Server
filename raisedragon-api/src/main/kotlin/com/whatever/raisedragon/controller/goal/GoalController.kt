package com.whatever.raisedragon.controller.goal

import com.whatever.raisedragon.applicationservice.goal.GoalApplicationService
import com.whatever.raisedragon.applicationservice.goal.dto.GoalResponse
import com.whatever.raisedragon.applicationservice.goal.dto.GoalRetrieveParticipantResponse
import com.whatever.raisedragon.applicationservice.goal.dto.GoalWithBettingResponse
import com.whatever.raisedragon.common.Response
import com.whatever.raisedragon.security.authentication.UserInfo
import com.whatever.raisedragon.security.resolver.GetAuth
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
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

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Creating Goal API",
        description = "다짐을 생성합니다.",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @PostMapping
    fun create(
        @Valid @RequestBody request: GoalCreateRequest,
        @GetAuth userinfo: UserInfo
    ): Response<GoalResponse> {
        return Response.success(
            goalApplicationService.createGoal(request.toServiceRequest(userinfo.id))
        )
    }

    @Operation(
        summary = "Retrieving Goal API",
        description = "다짐 단일 조회 및 요청한 사용자에 관한 정보 제공",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @GetMapping("/{goalId}")
    fun retrieveDetail(
        @PathVariable goalId: Long,
        @GetAuth userInfo: UserInfo
    ): Response<GoalWithBettingResponse> {
        return Response.success(goalApplicationService.retrieveGoalDetail(goalId, userInfo.id))
    }

    @Operation(
        summary = "Retrieving Goal API",
        description = "다짐 단일 조회"
    )
    @GetMapping("/{goalId}/simple")
    fun retrieveOne(
        @PathVariable goalId: Long
    ): Response<GoalResponse> {
        return Response.success(goalApplicationService.retrieveGoal(goalId))
    }

    @Operation(
        summary = "Retrieving GoalBetting API",
        description = "해당 다짐에 대한 모든 배팅 조회",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @GetMapping("/betting/{goalId}")
    fun retrieveParticipant(
        @PathVariable goalId: Long,
        @GetAuth userInfo: UserInfo
    ): Response<GoalRetrieveParticipantResponse> {
        return Response.success(
            goalApplicationService.retrieveGoalBettingParticipant(
                goalId = goalId
            )
        )
    }

    @Operation(
        summary = "Retrieving GoalBetting API [No Auth]",
        description = "해당 다짐에 대한 모든 배팅 조회 [No Auth]",
    )
    @GetMapping("/betting/{goalId}/no-auth")
    fun retrieveParticipantNoAuth(
        @PathVariable goalId: Long,
    ): Response<GoalRetrieveParticipantResponse> {
        return Response.success(
            goalApplicationService.retrieveGoalBettingParticipantNoAuth(
                goalId = goalId
            )
        )
    }

    @Operation(
        summary = "Modify Goal API",
        description = "다짐 세부 내용 수정",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @PutMapping("{goalId}")
    fun modifyGoal(
        @GetAuth userInfo: UserInfo,
        @PathVariable goalId: Long,
        @RequestBody goalModifyRequest: GoalModifyRequest
    ): Response<GoalResponse> {
        return Response.success(
            goalApplicationService.modifyGoal(
                goalModifyRequest.toServiceRequest(
                    userId = userInfo.id,
                    goalId = goalId,
                )
            )
        )
    }

    @Operation(
        summary = "Delete Goal API",
        description = "다짐 삭제",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @DeleteMapping
    fun deleteGoal(
        @GetAuth userInfo: UserInfo,
        @RequestBody goalDeleteRequest: GoalDeleteRequest
    ): Response<Unit> {
        return Response.success(
            goalApplicationService.deleteGoal(
                goalDeleteRequest.toServiceRequest(userInfo.id)
            )
        )
    }
}