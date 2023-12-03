package com.whatever.raisedragon.applicationservice

import com.whatever.raisedragon.applicationservice.dto.UserResponse
import com.whatever.raisedragon.controller.user.UserCreateUpdateResponse
import com.whatever.raisedragon.controller.user.UserRetrieveResponse
import com.whatever.raisedragon.domain.user.Nickname
import com.whatever.raisedragon.domain.user.User
import com.whatever.raisedragon.domain.user.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserApplicationService(
    private val userService: UserService
) {
    fun create(): UserCreateUpdateResponse {
        return UserCreateUpdateResponse(
            UserResponse.of(
                User(
                    0L,
                    "sample",
                    "sample",
                    Nickname("sample"),
                    null,
                    null,
                    null
                )
            )
        )
    }

    fun retrieve(): UserRetrieveResponse {
        val sampleUser = User(
            0L,
            "sample",
            "sample",
            Nickname("sample"),
            null,
            null,
            null
        )
        return UserRetrieveResponse(
            sampleUser.id,
            sampleUser.nickname
        )
    }
}