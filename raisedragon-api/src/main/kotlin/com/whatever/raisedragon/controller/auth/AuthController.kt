package com.whatever.raisedragon.controller.auth

import com.whatever.raisedragon.applicationservice.AuthApplicationService
import com.whatever.raisedragon.common.Response
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/auth")
class AuthController(
    private val authApplicationService: AuthApplicationService,
) {

    @Operation(summary = "LoginAPI", description = "Kakao Login")
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): Response<LoginResponse> {
        return Response.success(
            LoginResponse(authApplicationService.kakoLogin(loginRequest.accessToken))
        )
    }
}