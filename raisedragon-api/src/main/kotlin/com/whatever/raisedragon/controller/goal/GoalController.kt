package com.whatever.raisedragon.controller.goal

import com.whatever.raisedragon.applicationservice.GoalApplicationService
import com.whatever.raisedragon.common.Response
import com.whatever.raisedragon.domain.goal.Content
import com.whatever.raisedragon.domain.goal.Threshold
import com.whatever.raisedragon.domain.user.User
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

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creating Goal API", description = "다짐을 생성합니다.")
    @PostMapping
    fun create(
        @Valid @RequestBody request: GoalCreateRequest,
        user: User
    ): Response<GoalResponse> {
        return Response.success(
            GoalResponse.of(
                goalApplicationService.createGoal(
                    bettingType = request.type,
                    content = Content(request.content),
                    presignedURL = request.presig,
                    threshold = Threshold(request.threshold),
                    startDate = request.startDate,
                    endDate = request.endDate,
                    user = user
                )
            )
        )
    }

    @Operation(summary = "Deleting Goal API", description = "Delete Goal")
    @DeleteMapping("/{goalId}")
    fun delete(@PathVariable goalId: Long): Response<Unit> {
        // TODO: To get user id for using jwt
        return Response.success()
    }
}