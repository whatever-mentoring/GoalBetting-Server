package com.whatever.raisedragon.controller.goalproof

import com.whatever.raisedragon.applicationservice.GoalProofApplicationService
import com.whatever.raisedragon.common.Response
import com.whatever.raisedragon.common.aop.Auth
import com.whatever.raisedragon.common.aop.AuthContext
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "GoalProof", description = "GoalProof API")
@RestController
@RequestMapping("/v1/goal-proof")
class GoalProofController(
    private val goalProofApplicationService: GoalProofApplicationService
) {

    @Auth
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "GoalProof create API", description = "다짐 인증을 생성합니다.")
    @PostMapping
    fun create(
        @Valid @RequestBody goalProofCreateRequest: GoalProofCreateRequest,
        // @GetAuth userInfo: UserInfo
    ): Response<GoalProofCreateUpdateResponse> {
        return Response.success(
            goalProofApplicationService.create(
                userId = AuthContext.getUser().id!!,
                goalId = goalProofCreateRequest.goalId,
                url = goalProofCreateRequest.url,
                comment = goalProofCreateRequest.comment,
            )
        )
    }

    @Auth
    @Operation(summary = "Retrieving single GoalProof API", description = "단건 다짐 인증을 조회합니다")
    @GetMapping("/{goalProofId}")
    fun retrieve(
        @PathVariable goalProofId: Long
    ): Response<GoalProofRetrieveResponse> {
        AuthContext.getUser()
        return Response.success(goalProofApplicationService.retrieve(goalProofId))
    }

    @Auth
    @Operation(summary = "Retrieving GoalProofs API", description = "모든 다짐 인증을 조회합니다")
    @GetMapping
    fun retrieveAll(
        // @GetAuth userInfo: UserInfo,
        @RequestBody request: GoalProofRetrieveAllRequest
    ): Response<GoalProofListRetrieveResponse> {
        return Response.success(
            goalProofApplicationService.retrieveAll(
                request.goalId,
                AuthContext.getUser().id!!
            )
        )
    }

    @Auth
    @Operation(summary = "Updating GoalProof API", description = "다짐 인증을 수정합니다")
    @PutMapping("/{goalProofId}")
    fun update(
        @PathVariable goalProofId: Long,
        @RequestBody request: GoalProofUpdateRequest,
        // @GetAuth userInfo: UserInfo
    ): Response<GoalProofRetrieveResponse> {
        return Response.success(
            goalProofApplicationService.update(
                goalProofId = goalProofId,
                userId = AuthContext.getUser().id!!,
                url = request.url,
                comment = request.comment
            )
        )
    }

}