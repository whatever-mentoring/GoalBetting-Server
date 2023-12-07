package com.whatever.raisedragon.domain.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByOauthTokenPayload(payload: String): UserEntity?
}