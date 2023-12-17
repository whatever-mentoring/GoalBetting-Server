package com.whatever.raisedragon.controller.user

import com.whatever.raisedragon.applicationservice.UserApplicationService
import com.whatever.raisedragon.common.Response
import com.whatever.raisedragon.common.aop.Auth
import com.whatever.raisedragon.common.aop.AuthContext
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "User", description = "User API")
@RestController
@RequestMapping("/v1/user")
class UserController(
    private val userApplicationService: UserApplicationService
) {

    @Auth
    @Operation(summary = "Request User retrieve API", description = "요청한 사용자를 조회합니다.")
    @GetMapping
    fun retrieveMe(
        // @GetAuth userInfo: UserInfo
    ): Response<UserRetrieveResponse> {
        return Response.success(
            userApplicationService.retrieve(
                AuthContext.getUser().id!!
            )
        )
    }

    @Auth
    @Operation(summary = "User retrieve API", description = "특정 사용자를 조회합니다.")
    @GetMapping("{userId}")
    fun retrieveTarget(
        // @GetAuth userInfo: UserInfo,
        @PathVariable userId: Long
    ): Response<UserRetrieveResponse> {
        return Response.success(
            userApplicationService.retrieve(
                AuthContext.getUser().id!!
            )
        )
    }

    @Auth
    @Operation(summary = "User Nickname update API", description = "닉네임을 수정합니다.")
    @PutMapping
    fun updateNickname(
        // @GetAuth userInfo: UserInfo,
        @RequestBody userNicknameUpdateRequest: UserNicknameUpdateRequest
    ): Response<UserRetrieveResponse> {
        return Response.success(
            userApplicationService.updateNickname(
                id = AuthContext.getUser().id!!,
                nickname = userNicknameUpdateRequest.nickname
            )
        )
    }

    @Auth
    @Operation(summary = "User delete API", description = "회원탈퇴")
    @DeleteMapping
    fun delete(
        // @GetAuth userInfo: UserInfo,
    ): Response<Unit> {
        return Response.success(
            userApplicationService.delete(
                AuthContext.getUser().id!!
            )
        )
    }
}