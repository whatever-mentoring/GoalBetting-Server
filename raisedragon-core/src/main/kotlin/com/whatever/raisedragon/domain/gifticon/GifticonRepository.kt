package com.whatever.raisedragon.domain.gifticon

import com.whatever.raisedragon.domain.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GifticonRepository : JpaRepository<GifticonEntity, Long> {
    fun findAllByUserEntity(userEntity: UserEntity): List<GifticonEntity>
}