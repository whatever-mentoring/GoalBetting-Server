package com.whatever.raisedragon.domain.goalgifticon

import com.whatever.raisedragon.domain.goal.GoalEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GoalGifticonRepository : JpaRepository<GoalGifticonEntity, Long> {
    fun findByGoalEntity(goalEntity: GoalEntity): GoalGifticonEntity
}