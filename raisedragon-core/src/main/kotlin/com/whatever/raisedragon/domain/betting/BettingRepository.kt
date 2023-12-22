package com.whatever.raisedragon.domain.betting

import com.whatever.raisedragon.domain.goal.GoalEntity
import com.whatever.raisedragon.domain.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface BettingRepository : JpaRepository<BettingEntity, Long> {

    fun findByUserEntityAndGoalEntity(
        userEntity: UserEntity,
        goalEntity: GoalEntity
    ): BettingEntity?

    fun findAllByGoalEntity(goalEntity: GoalEntity): List<BettingEntity>

    fun findAllByGoalEntityAndDeletedAtIsNull(goalEntity: GoalEntity): List<BettingEntity>

    fun findAllByUserEntity(userEntity: UserEntity): List<BettingEntity>

    @Modifying
    @Query("update BettingEntity b set b.result=:result where b.id in :ids")
    fun bulkModifyingByResultWhereIdInIds(result: Result, ids: Set<Long>): Int
}