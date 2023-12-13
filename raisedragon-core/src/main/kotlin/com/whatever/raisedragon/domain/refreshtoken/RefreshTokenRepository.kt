package com.whatever.raisedragon.domain.refreshtoken

import com.whatever.raisedragon.domain.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshTokenEntity, Long> {
    fun findByPayload(payload: String): RefreshTokenEntity?

    fun findByUserEntity(userEntity: UserEntity): RefreshTokenEntity?
}