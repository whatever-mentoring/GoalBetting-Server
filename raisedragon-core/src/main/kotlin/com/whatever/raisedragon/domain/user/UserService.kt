package com.whatever.raisedragon.domain.user

import com.whatever.raisedragon.common.exception.BaseException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.Supplier

@Service
@Transactional(readOnly = true)
class UserService(
    @Qualifier("notFoundExceptionSupplier") private val notFoundExceptionSupplier: Supplier<BaseException>,
    private val userRepository: UserRepository,
) {

    fun loadByOAuthPayload(payload: String): User? {
        return userRepository.findByOauthTokenPayload(payload)?.toDto()
    }

    fun loadById(id: Long): User {
        return userRepository.findById(id).orElseThrow(notFoundExceptionSupplier).toDto()
    }

    fun findAllByIdInIds(ids: Set<Long>): List<User> {
        return userRepository.findAllById(ids).map { it.toDto() }
    }

    fun isNicknameDuplicated(nickname: String): Boolean {
        return userRepository.existsByNickname(Nickname(nickname))
    }

    @Transactional
    fun create(user: User): User {
        return userRepository.save(user.fromDto()).toDto()
    }

    @Transactional
    fun updateNickname(id: Long, nickname: String): User {
        val userEntity = userRepository.findById(id).orElseThrow(notFoundExceptionSupplier)
        userEntity.nickname = Nickname(nickname)
        return userEntity.toDto()
    }

    @Transactional
    fun softDelete(id: Long) {
        val userEntity = loadById(id).fromDto()
        userEntity.disable()
    }

    @Transactional
    fun hardDeleteById(id: Long) {
        val userEntity = loadById(id).fromDto()
        userRepository.delete(userEntity)
    }

    @Transactional
    fun convertBySoftDeleteToEntity(id: Long) {
        val userEntity = loadById(id).fromDto()
        userEntity.able()
    }
}