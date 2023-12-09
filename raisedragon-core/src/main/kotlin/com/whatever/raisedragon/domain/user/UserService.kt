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
        return userRepository.findByIdOrNull(id)?.toDto() ?: throw Exception()
    }

    fun create(user: User): User {
        return userRepository.save(user.fromDto()).toDto()
    }
}