package com.whatever.raisedragon.domain.user

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
) {

    @Transactional(readOnly = true)
    fun loadByOAuthPayload(payload: String): User? {
        return userRepository.findByOauthTokenPayload(payload)?.toDto()
    }

    @Transactional(readOnly = true)
    fun loadById(id: Long): User {
        return userRepository.findByIdOrNull(id)?.toDto() ?: throw IllegalArgumentException("유저를 불러오는 중, 잘못된 값을 요청하셨습니다.")
    }

    fun create(user: User): User {
        return userRepository.save(user.fromDto()).toDto()
    }

    fun updateNickname(id: Long, nickname: String): User {
        val userEntity = userRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("유저를 불러오는 중, 잘못된 값을 요청하셨습니다.")
        userEntity.nickname = Nickname(nickname)
        return userEntity.toDto()
    }

    fun softDelete(id: Long) {
        val userEntity = loadById(id).fromDto()
        userEntity.disable()
    }
}