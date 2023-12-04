package com.whatever.raisedragon.controller.goalcheering

import com.whatever.raisedragon.common.Response
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "Goal-Cheering", description = "Goal-Cheering API")
@RestController
@RequestMapping("/v1/cheering")
class GoalCheeringController {

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creating GoalCheering API", description = "Create GoalCheering")
    @PostMapping
    fun create(
        @Valid @RequestBody request: CreateGoalCheeringRequest
    ): Response<GoalCheeringResponse> {
        return Response.success(GoalCheeringResponse.sample())
    }

    @Operation(summary = "Updating GoalCheering API", description = "Update GoalCheering")
    @PutMapping
    fun update(
        @Valid @RequestBody request: UpdateGoalCheeringRequest
    ): Response<GoalCheeringResponse> {
        return Response.success(GoalCheeringResponse.sample())
    }

    @Operation(summary = "Deleting GoalCheering API", description = "Delete GoalCheering")
    @DeleteMapping("/{cheeringId}")
    fun delete(@PathVariable cheeringId: Long): Response<Unit> {
        return Response.success()
    }
}