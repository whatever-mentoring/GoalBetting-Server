package com.whatever.raisedragon.controller.goalproof

import com.whatever.raisedragon.applicationservice.GoalProofApplicationService
import com.whatever.raisedragon.common.Response
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
    @Operation(summary = "GoalProof create API", description = "Create GoalProof")
    @PostMapping
    fun create(
        @Valid @RequestBody goalProofCreateRequest: GoalProofCreateRequest
    ): Response<GoalProofCreateUpdateResponse> {
        return Response.success(goalProofApplicationService.create())
    }

    @Operation(summary = "GoalProof retrieve API", description = "Retrieve GoalProof")
    @GetMapping("/{goalProofId}")
    fun retrieve(
        @PathVariable goalProofId: Long
    ): Response<GoalProofRetrieveResponse> {
        return Response.success(goalProofApplicationService.retrieve())
    }

    @Operation(summary = "GoalProof update API", description = "Update GoalProof")
    @PutMapping
    fun update(
        @RequestBody goalProofUpdateRequest: GoalProofUpdateRequest
    ): Response<GoalProofCreateUpdateResponse> {
        return Response.success(goalProofApplicationService.create())
    }

    @Operation(summary = "GoalProof delete API", description = "Delete GoalProof")
    @DeleteMapping("{goalProofId}")
    fun delete(
        @PathVariable goalProofId: Long
    ): Response<Unit> {
        return Response.success()
    }
}