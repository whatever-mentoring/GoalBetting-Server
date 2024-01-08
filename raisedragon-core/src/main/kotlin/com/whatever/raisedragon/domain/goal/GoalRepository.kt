package com.whatever.raisedragon.domain.goal

import com.whatever.raisedragon.domain.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface GoalRepository : JpaRepository<GoalEntity, Long> {
    fun findAllByUserEntity(userEntity: UserEntity): List<GoalEntity>

    fun findAllByEndDateLessThanEqualAndGoalResultIs(endDate: LocalDateTime, goalResult: GoalResult): List<GoalEntity>

    fun findAllByUserEntityAndGoalResult(userEntity: UserEntity, goalResult: GoalResult): List<GoalEntity>
    fun existsByUserEntityAndEndDateIsAfter(userEntity: UserEntity, now: LocalDateTime): Boolean
}