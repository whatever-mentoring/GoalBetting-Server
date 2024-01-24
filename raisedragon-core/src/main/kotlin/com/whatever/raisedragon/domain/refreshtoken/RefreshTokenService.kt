package com.whatever.raisedragon.domain.refreshtoken

import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.domain.user.User
import com.whatever.raisedragon.domain.user.UserEntity
import com.whatever.raisedragon.domain.user.UserRepository
import com.whatever.raisedragon.domain.user.fromDto
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.Supplier

@Service
@Transactional(readOnly = true)
class RefreshTokenService(
    @Qualifier("notFoundExceptionSupplier") private val notFoundExceptionSupplier: Supplier<BaseException>,
    @Qualifier("internalExceptionSupplier") private val internalExceptionSupplier: Supplier<BaseException>,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userRepository: UserRepository
) {

    @Transactional
    fun create(refreshToken: RefreshToken, userEntity: UserEntity) {
        refreshTokenRepository.save(refreshToken.fromDto(userEntity))
    }

    fun loadByPayload(payload: String): RefreshToken? {
        return refreshTokenRepository.findByPayload(payload)?.toDto()
    }

    fun loadByUser(user: User): RefreshTokenEntity? {
        return refreshTokenRepository.findByUserEntity(user.fromDto())
    }

    @Transactional
    fun updatePayloadByUserId(userId: Long, payload: String) {
        val userEntity = userRepository.findById(userId).orElseThrow(notFoundExceptionSupplier)
        val refreshTokenEntity = refreshTokenRepository.findByUserEntity(userEntity)
        refreshTokenEntity?.payload = payload
    }

    @Transactional
    fun hardDeleteByUserId(userId: Long) {
        val refreshTokenEntity = refreshTokenRepository.findByUserEntity(
            userEntity = userRepository.findById(userId).orElseThrow(notFoundExceptionSupplier)
        ) ?: throw internalExceptionSupplier.get()
        refreshTokenRepository.delete(refreshTokenEntity)
    }
}