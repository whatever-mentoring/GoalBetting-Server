package com.whatever.raisedragon.controller.goalcheering

import com.whatever.raisedragon.common.Response
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Goal-Cheering", description = "Goal-Cheering API")
@RestController
@RequestMapping("/v1/cheering")
class GoalCheeringController {

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