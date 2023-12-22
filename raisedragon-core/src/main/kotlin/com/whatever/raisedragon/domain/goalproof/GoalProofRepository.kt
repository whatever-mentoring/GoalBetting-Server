package com.whatever.raisedragon.domain.goalproof

import com.whatever.raisedragon.domain.goal.GoalEntity
import com.whatever.raisedragon.domain.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GoalProofRepository : JpaRepository<GoalProofEntity, Long> {
    fun findAllByUserEntityAndGoalEntity(userEntity: UserEntity, goalEntity: GoalEntity): List<GoalProofEntity>

    fun countAllByGoalEntity(goalEntity: GoalEntity): Int
}