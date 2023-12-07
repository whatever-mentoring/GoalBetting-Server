package com.whatever.raisedragon.domain.user

import com.whatever.raisedragon.domain.auth.AuthService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val authService: AuthService
) {


    @Transactional(readOnly = true)
    fun loadByOAuthPayload(payload: String): User? {
        return userRepository.findByOauthTokenPayload(payload)!!.toDto()
    }

    fun create(user: User): User {
        return userRepository.save(user.fromDto()).toDto()
    }
}