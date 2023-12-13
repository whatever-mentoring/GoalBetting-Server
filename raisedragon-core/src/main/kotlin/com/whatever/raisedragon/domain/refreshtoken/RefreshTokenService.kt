package com.whatever.raisedragon.domain.refreshtoken

import com.whatever.raisedragon.domain.user.User
import com.whatever.raisedragon.domain.user.UserEntity
import com.whatever.raisedragon.domain.user.fromDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class RefreshTokenService(
    private val refreshTokenRepository: RefreshTokenRepository
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
    fun updatePayloadByVo(refreshToken: RefreshToken, userEntity: UserEntity) {
        val refreshTokenEntity = refreshToken.fromDto(userEntity)
    }
}