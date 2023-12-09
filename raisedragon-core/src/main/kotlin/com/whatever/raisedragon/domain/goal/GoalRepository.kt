package com.whatever.raisedragon.domain.goal

import com.whatever.raisedragon.domain.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GoalRepository : JpaRepository<GoalEntity, Long> {
    fun findAllByUserEntity(userEntity: UserEntity): List<GoalEntity>
}