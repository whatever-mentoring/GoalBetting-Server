package com.whatever.raisedragon.domain.winner

import com.whatever.raisedragon.domain.goal.GoalEntity
import com.whatever.raisedragon.domain.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface WinnerRepository : JpaRepository<WinnerEntity, Long> {
    fun findByGoalEntity(goalEntity: GoalEntity): WinnerEntity?

    fun findByGoalEntityAndUserEntity(goalEntity: GoalEntity, userEntity: UserEntity): WinnerEntity?
}