package com.whatever.raisedragon.applicationservice

import com.whatever.raisedragon.controller.user.UserRetrieveResponse
import com.whatever.raisedragon.domain.user.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserApplicationService(
    private val userService: UserService
) {

    fun retrieve(id: Long): UserRetrieveResponse {
        val user = userService.loadById(id)
        return UserRetrieveResponse(
            userId = user.id!!,
            nickname = user.nickname,
            nicknameIsModified = user.createdAt!! < user.updatedAt
        )
    }

    @Transactional
    fun updateNickname(id: Long, nickname: String): UserRetrieveResponse {
        val user = userService.updateNickname(id, nickname)
        return UserRetrieveResponse(
            userId = user.id!!,
            nickname = user.nickname,
            nicknameIsModified = user.createdAt!! < user.updatedAt
        )
    }

    fun delete(id: Long) {
        userService.softDelete(id)
    }
}