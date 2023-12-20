package com.whatever.raisedragon.controller.goalproof

import com.whatever.raisedragon.applicationservice.GoalProofApplicationService
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
@RequestMapping("/v1/goal-proof")
@SecurityRequirement(name = "Authorization")
class GoalProofController(
    private val goalProofApplicationService: GoalProofApplicationService
) {

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "GoalProof create API", description = "다짐 인증을 생성합니다.")
    @PostMapping
    fun create(
        @Valid @RequestBody goalProofCreateRequest: GoalProofCreateRequest,
        @GetAuth userInfo: UserInfo
    ): Response<GoalProofCreateUpdateResponse> {
        return Response.success(
            goalProofApplicationService.create(
                userId = userInfo.id,
                goalId = goalProofCreateRequest.goalId,
                url = goalProofCreateRequest.url,
                comment = goalProofCreateRequest.comment,
            )
        )
    }

    @Operation(summary = "Retrieving single GoalProof API", description = "단건 다짐 인증을 조회합니다")
    @GetMapping("/{goalProofId}")
    fun retrieve(
        @PathVariable goalProofId: Long
    ): Response<GoalProofRetrieveResponse> {
        return Response.success(goalProofApplicationService.retrieve(goalProofId))
    }

    @Operation(summary = "Retrieving GoalProofs API", description = "모든 다짐 인증을 조회합니다")
    @GetMapping
    fun retrieveAll(
        @GetAuth userInfo: UserInfo,
        @RequestBody request: GoalProofRetrieveAllRequest
    ): Response<GoalProofListRetrieveResponse> {
        return Response.success(goalProofApplicationService.retrieveAll(request.goalId, userInfo.id))
    }

    @Operation(summary = "Updating GoalProof API", description = "다짐 인증을 수정합니다")
    @PutMapping("/{goalProofId}")
    fun update(
        @PathVariable goalProofId: Long,
        @RequestBody request: GoalProofUpdateRequest,
        @GetAuth userInfo: UserInfo
    ): Response<GoalProofRetrieveResponse> {
        return Response.success(
            goalProofApplicationService.update(
                goalProofId = goalProofId,
                userId = userInfo.id,
                url = request.url,
                comment = request.comment
            )
        )
    }

}