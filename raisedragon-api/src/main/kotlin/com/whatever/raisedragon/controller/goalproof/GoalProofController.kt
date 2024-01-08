package com.whatever.raisedragon.controller.goalproof

import com.whatever.raisedragon.applicationservice.goalproof.GoalProofApplicationService
import com.whatever.raisedragon.applicationservice.goalproof.dto.GoalProofCreateUpdateResponse
import com.whatever.raisedragon.applicationservice.goalproof.dto.GoalProofListRetrieveResponse
import com.whatever.raisedragon.applicationservice.goalproof.dto.GoalProofRetrieveResponse
import com.whatever.raisedragon.common.Response
import com.whatever.raisedragon.security.authentication.UserInfo
import com.whatever.raisedragon.security.resolver.GetAuth
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "GoalProof", description = "GoalProof API")
@RestController
@RequestMapping("/v1")
@SecurityRequirement(name = "Authorization")
class GoalProofController(
    private val goalProofApplicationService: GoalProofApplicationService
) {

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "GoalProof create API", description = "다짐 인증을 생성합니다.")
    @PostMapping("/goal-proof")
    fun create(
        @Valid @RequestBody goalProofCreateRequest: GoalProofCreateRequest,
        @GetAuth userInfo: UserInfo
    ): Response<GoalProofCreateUpdateResponse> {
        return Response.success(
            goalProofApplicationService.create(goalProofCreateRequest.toServiceRequest(userInfo.id))
        )
    }

    @Operation(summary = "Retrieving single GoalProof API", description = "단건 다짐 인증을 조회합니다")
    @GetMapping("/goal-proof/{goalProofId}")
    fun retrieve(
        @PathVariable goalProofId: Long
    ): Response<GoalProofRetrieveResponse> {
        return Response.success(goalProofApplicationService.retrieve(goalProofId))
    }

    @Operation(summary = "Retrieving GoalProofs API", description = "모든 다짐 인증을 조회합니다")
    @GetMapping("goal/{goalId}/goal-proof")
    fun retrieveAll(
        @GetAuth userInfo: UserInfo,
        @PathVariable goalId: Long
    ): Response<GoalProofListRetrieveResponse> {
        return Response.success(goalProofApplicationService.retrieveAll(goalId, userInfo.id))
    }

    @Operation(summary = "Retrieving GoalProofs' result API", description = "해당 다짐이 성공했는지 여부를 알려줍니다")
    @GetMapping("goal/{goalId}/goal-proof/result")
    fun isGoalSuccess(
        @GetAuth userInfo: UserInfo,
        @PathVariable goalId: Long
    ): Response<Boolean> {
        return Response.success(goalProofApplicationService.isSuccess(goalId, userInfo.id))
    }

    @Operation(summary = "Updating GoalProof API", description = "다짐 인증을 수정합니다")
    @PutMapping("/goal-proof/{goalProofId}")
    fun update(
        @PathVariable goalProofId: Long,
        @RequestBody request: GoalProofUpdateRequest,
        @GetAuth userInfo: UserInfo
    ): Response<GoalProofRetrieveResponse> {
        return Response.success(
            goalProofApplicationService.update(
                request.toServiceRequest(userId = userInfo.id, goalProofId = goalProofId)
            )
        )
    }

}