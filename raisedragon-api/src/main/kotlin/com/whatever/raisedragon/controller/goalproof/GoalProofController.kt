package com.whatever.raisedragon.controller.goalproof

import com.whatever.raisedragon.applicationservice.GoalProofApplicationService
import com.whatever.raisedragon.common.Response
import com.whatever.raisedragon.domain.goalproof.Document
import com.whatever.raisedragon.security.authentication.UserInfo
import com.whatever.raisedragon.security.resolver.GetAuth
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
                document = Document(goalProofCreateRequest.document)
            )
        )
    }

    @Operation(summary = "GoalProof retrieve API", description = "Retrieve GoalProof")
    @GetMapping("/{goalProofId}")
    fun retrieve(
        @PathVariable goalProofId: Long
    ): Response<GoalProofRetrieveResponse> {
        return Response.success(goalProofApplicationService.retrieve())
    }
}