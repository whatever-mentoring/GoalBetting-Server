package com.whatever.raisedragon.domain.winner

import com.whatever.raisedragon.domain.goal.GoalEntity
import org.springframework.data.jpa.repository.JpaRepository

interface WinnerRepository : JpaRepository<WinnerEntity, Long> {
    fun findByGoalEntity(goalEntity: GoalEntity): WinnerEntity?
}