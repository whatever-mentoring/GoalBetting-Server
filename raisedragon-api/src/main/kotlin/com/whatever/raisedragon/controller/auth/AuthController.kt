package com.whatever.raisedragon.controller.auth

import com.whatever.raisedragon.applicationservice.auth.AuthApplicationService
import com.whatever.raisedragon.applicationservice.auth.dto.LoginResponse
import com.whatever.raisedragon.applicationservice.auth.dto.TokenRefreshResponse
import com.whatever.raisedragon.common.Response
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "Auth", description = "Auth API")
@RestController
@RequestMapping("/v1/auth")
class AuthController(
    private val authApplicationService: AuthApplicationService,
) {

    @Operation(summary = "Login API", description = "Kakao Login")
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): Response<LoginResponse> {
        return Response.success(authApplicationService.kakaoLogin(loginRequest.toServiceRequest()))
    }

    @Operation(
        summary = "Token Refresh API",
        description = "토큰 갱신",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @PostMapping("/refresh")
    fun reissue(@RequestHeader refreshToken: String): Response<TokenRefreshResponse> {
        return Response.success(authApplicationService.reissueToken(refreshToken))
    }
}