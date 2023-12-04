package com.whatever.raisedragon.controller.goal

import com.whatever.raisedragon.common.Response
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "Goal", description = "Goal API")
@RestController
@RequestMapping("/v1/goal")
class GoalController {

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creating Goal API", description = "Create Goal")
    @PostMapping
    fun create(
        @Valid @RequestBody request: GoalCreateRequest
    ): Response<GoalResponse> {
        return Response.success(GoalResponse.sample())
    }

    @Operation(summary = "Deleting Goal API", description = "Delete Goal")
    @DeleteMapping("/{goalId}")
    fun delete(@PathVariable goalId: Long): Response<Unit> {
        // TODO: To get user id for using jwt
        return Response.success()
    }
}