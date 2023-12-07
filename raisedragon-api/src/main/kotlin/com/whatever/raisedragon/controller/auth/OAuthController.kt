package com.whatever.raisedragon.controller.auth

import com.whatever.raisedragon.applicationservice.AuthApplicationService
import com.whatever.raisedragon.common.Response
import com.whatever.raisedragon.controller.auth.dto.LoginRequest
import com.whatever.raisedragon.controller.auth.dto.LoginResponse
import com.whatever.raisedragon.domain.auth.KakaoOAuthService
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.view.RedirectView


@RestController
@RequestMapping("/v1/auth")
class OAuthController(
    private val authApplicationService: AuthApplicationService,
    private val kakaoOAuthService: KakaoOAuthService
) {

    @GetMapping("/entry")
    fun entry(): RedirectView {
        return kakaoOAuthService.entryKakao()
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): Response<LoginResponse> {
        return Response.success(
            authApplicationService.kakoLogin(loginRequest.code)
                .oauthTokenPayload?.let { LoginResponse(it) }
        )
    }
}