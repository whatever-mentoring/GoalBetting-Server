package com.whatever.raisedragon.controller.jwt

import com.whatever.raisedragon.common.Response
import com.whatever.raisedragon.domain.user.UserService
import com.whatever.raisedragon.security.jwt.JwtAgent
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "JWT", description = "Providing test token API")
@RequestMapping("/v1/token")
@RestController
class TokenController(private val jwtAgent: JwtAgent, private val userService: UserService) {

    @Operation(description = "테스트 토큰 발급")
    @GetMapping("/{userId}")
    fun getToken(@PathVariable userId: Long): Response<String> {
        return Response.success(jwtAgent.provide(userService.loadById(userId)).accessToken)
    }
}