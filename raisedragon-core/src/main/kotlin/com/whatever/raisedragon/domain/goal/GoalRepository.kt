package com.whatever.raisedragon.domain.goal

import com.whatever.raisedragon.domain.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface GoalRepository : JpaRepository<GoalEntity, Long> {
    fun findAllByUserEntity(userEntity: UserEntity): List<GoalEntity>

    fun findByIdAndUserEntity(id: Long, userEntity: UserEntity): GoalEntity?

    fun findAllByEndDateLessThanEqualAndResultIs(endDate: LocalDateTime, result: Result): List<GoalEntity>

    fun findAllByUserEntityAndResult(userEntity: UserEntity, result: Result): List<GoalEntity>
    fun existsByUserEntityAndEndDateIsAfter(userEntity: UserEntity, now: LocalDateTime): Boolean
}