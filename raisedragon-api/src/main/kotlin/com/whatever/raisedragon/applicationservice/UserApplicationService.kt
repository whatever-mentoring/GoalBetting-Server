package com.whatever.raisedragon.applicationservice

import com.whatever.raisedragon.controller.user.UserCreateUpdateResponse
import com.whatever.raisedragon.controller.user.UserRetrieveResponse
import com.whatever.raisedragon.domain.user.Nickname
import com.whatever.raisedragon.domain.user.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserApplicationService(
    private val userService: UserService
) {
    @Transactional
    fun create(): UserCreateUpdateResponse {
        return UserCreateUpdateResponse(
            UserRetrieveResponse(
                userId = 0L,
                nickname = Nickname("sample")
            )
        )
    }

    fun retrieve(): UserRetrieveResponse {
        return UserRetrieveResponse(
            userId = 0L,
            nickname = Nickname("sample")
        )
    }
}