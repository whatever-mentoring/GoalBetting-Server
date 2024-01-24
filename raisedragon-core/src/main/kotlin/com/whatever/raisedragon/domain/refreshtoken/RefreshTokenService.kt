package com.whatever.raisedragon.domain.refreshtoken

import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.domain.user.UserRepository
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
    fun create(userId: Long, payload: String?): RefreshToken {
        val userEntity = userRepository.findById(userId).orElseThrow(notFoundExceptionSupplier)
        return refreshTokenRepository.save(RefreshTokenEntity(userEntity, payload)).toDto()
    }

    fun findByPayload(payload: String): RefreshToken? {
        return refreshTokenRepository.findByPayload(payload)?.toDto()
    }

    fun findByUserId(userId: Long): RefreshToken? {
        val userEntity = userRepository.findById(userId).orElseThrow(notFoundExceptionSupplier)
        return refreshTokenRepository.findByUserEntity(userEntity)?.toDto()
    }

    @Transactional
    fun updatePayloadByUserId(userId: Long, payload: String): RefreshToken {
        val userEntity = userRepository.findById(userId).orElseThrow(notFoundExceptionSupplier)
        val refreshTokenEntity = refreshTokenRepository.findByUserEntity(userEntity)
            ?: throw notFoundExceptionSupplier.get()
        refreshTokenEntity.payload = payload
        return refreshTokenEntity.toDto()
    }

    @Transactional
    fun hardDeleteByUserId(userId: Long) {
        val refreshTokenEntity = refreshTokenRepository.findByUserEntity(
            userEntity = userRepository.findById(userId).orElseThrow(notFoundExceptionSupplier)
        ) ?: throw internalExceptionSupplier.get()
        refreshTokenRepository.delete(refreshTokenEntity)
    }
}