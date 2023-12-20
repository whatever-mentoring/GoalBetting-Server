package com.whatever.raisedragon.controller.user

import com.whatever.raisedragon.applicationservice.UserApplicationService
import com.whatever.raisedragon.common.Response
import com.whatever.raisedragon.common.aop.AuthContext
import com.whatever.raisedragon.security.authentication.UserInfo
import com.whatever.raisedragon.security.resolver.GetAuth
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "User", description = "User API")
@RestController
@RequestMapping("/v1/user")
@SecurityRequirement(name = "Authorization")
class UserController(
    private val userApplicationService: UserApplicationService
) {

    @Operation(summary = "Request User retrieve API", description = "요청한 사용자를 조회합니다.")
    @GetMapping
    fun retrieveMe(
        @GetAuth userInfo: UserInfo
    ): Response<UserRetrieveResponse> {
        return Response.success(
            userApplicationService.retrieve(
                AuthContext.getUser().id!!
            )
        )
    }

    @Operation(summary = "User retrieve API", description = "특정 사용자를 조회합니다.")
    @GetMapping("{userId}")
    fun retrieveTarget(
        @GetAuth userInfo: UserInfo,
        @PathVariable userId: Long
    ): Response<UserRetrieveResponse> {
        return Response.success(
            userApplicationService.retrieve(
                AuthContext.getUser().id!!
            )
        )
    }

    @Operation(
        summary = "User Nickname update API",
        description = "닉네임을 수정합니다."
    )
    @PutMapping
    fun updateNickname(
        @GetAuth userInfo: UserInfo,
        @RequestBody userNicknameUpdateRequest: UserNicknameUpdateRequest
    ): Response<UserRetrieveResponse> {
        return Response.success(
            userApplicationService.updateNickname(
                id = userInfo.id,
                nickname = userNicknameUpdateRequest.nickname
            )
        )
    }

    @Operation(summary = "User delete API", description = "회원탈퇴")
    @DeleteMapping
    fun delete(
        @GetAuth userInfo: UserInfo,
    ): Response<Unit> {
        return Response.success(userApplicationService.delete(userInfo.id))
    }
}