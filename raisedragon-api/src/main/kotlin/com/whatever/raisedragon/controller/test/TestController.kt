package com.whatever.raisedragon.controller.test

import com.whatever.raisedragon.applicationservice.TestApplicationService
import com.whatever.raisedragon.common.Response
import com.whatever.raisedragon.domain.user.UserService
import com.whatever.raisedragon.security.jwt.JwtAgent
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "Test", description = "Providing various testing API")
@RequestMapping("/v1/test")
@RestController
class TestController(
    private val jwtAgent: JwtAgent,
    private val userService: UserService,
    private val testApplicationService: TestApplicationService
) {

    @Operation(description = "테스트 토큰 발급")
    @GetMapping("/token/{userId}")
    fun getToken(@PathVariable userId: Long): Response<String> {
        return Response.success(jwtAgent.provide(userService.loadById(userId)).accessToken)
    }

    @Operation(description = "단일 다짐에 대한 결과 확정")
    @PostMapping("/confirm-goal/{goalId}")
    fun confirmGoal(@PathVariable goalId: Long): Response<Unit> {
        testApplicationService.confirmGoalResult(goalId)
        return Response.success()
    }
}