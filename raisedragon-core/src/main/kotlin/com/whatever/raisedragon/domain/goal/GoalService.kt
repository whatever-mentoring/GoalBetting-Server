package com.whatever.raisedragon.domain.goal

import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.domain.user.User
import com.whatever.raisedragon.domain.user.UserEntity
import com.whatever.raisedragon.domain.user.UserRepository
import com.whatever.raisedragon.domain.user.fromDto
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.function.Supplier

@Transactional(readOnly = true)
@Service
class GoalService(
    @Qualifier("notFoundExceptionSupplier") private val notFoundExceptionSupplier: Supplier<BaseException>,
    private val goalRepository: GoalRepository,
    private val userRepository: UserRepository
) {

    @Transactional
    fun create(
        userId: Long,
        content: Content,
        goalType: GoalType,
        threshold: Threshold,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): Goal {
        val goal = goalRepository.save(
            GoalEntity(
                userEntity = userRepository.findById(userId).orElseThrow(notFoundExceptionSupplier),
                goalType = goalType,
                content = content,
                threshold = threshold,
                goalResult = GoalResult.PROCEEDING,
                startDate = startDate,
                endDate = endDate
            )
        )
        return goal.toDto()
    }

    fun loadById(id: Long): Goal {
        return goalRepository.findById(id).orElseThrow(notFoundExceptionSupplier).toDto()
    }

    fun existsByUserIdAndAnyResult(userId: Long, goalResult: GoalResult): Boolean {
        val userEntity = userRepository.findById(userId).orElseThrow(notFoundExceptionSupplier)
        return goalRepository.findAllByUserEntityAndGoalResult(userEntity, goalResult).isNotEmpty()
    }

    fun loadAllByUserId(userId: Long): List<Goal> {
        return goalRepository.findAllByUserEntity(
            userRepository.findById(userId).orElseThrow(notFoundExceptionSupplier)
        ).map { it.toDto() }
    }

    fun findAllByIds(goalIds: Set<Long>): List<Goal> {
        return goalRepository.findAllById(goalIds).map { it.toDto() }
    }

    fun findAllByEndDateLessThanEqualAndResultIsProceeding(endDate: LocalDateTime): List<Goal> {
        return goalRepository.findAllByEndDateLessThanEqualAndGoalResultIs(endDate, GoalResult.PROCEEDING)
            .map { it.toDto() }
    }

    @Transactional
    fun updateResult(goalId: Long, goalResult: GoalResult): Goal {
        val goalEntity = goalRepository.findById(goalId).orElseThrow(notFoundExceptionSupplier)
        goalEntity.goalResult = goalResult
        return goalEntity.toDto()
    }

    @Transactional
    fun modify(
        goal: Goal,
        userEntity: UserEntity,
        content: Content,
    ): Goal {
        val goalEntity = goal.fromDto(userEntity)
        goalEntity.content = content

        return goalEntity.toDto()
    }

    @Transactional
    fun softDelete(
        goal: Goal,
        userEntity: UserEntity,
    ): Goal {
        val goalEntity = goal.fromDto(userEntity)
        goalEntity.deletedAt = LocalDateTime.now()

        return goalEntity.toDto()
    }

    @Transactional
    fun hardDelete(
        goal: Goal,
        userEntity: UserEntity,
    ) {
        val goalEntity = goal.fromDto(userEntity)
        goalRepository.delete(goalEntity)
    }

    @Transactional
    fun hardDeleteByUserId(userId: Long) {
        val goals = goalRepository.findAllByUserEntity(
            userEntity = userRepository.findById(userId).orElseThrow(notFoundExceptionSupplier)
        )
        goalRepository.deleteAll(goals)
    }

    @Transactional
    fun increaseThreshold(goal: Goal, userEntity: UserEntity) {
        val goalEntity = goal.fromDto(userEntity)
        goalEntity.threshold = Threshold(goalEntity.threshold.value + 1)
    }

    fun findProceedingGoalIsExistsByUser(user: User): Boolean {
        val existsByUserEntityAndEndDateIsAfter = goalRepository.existsByUserEntityAndEndDateIsAfter(
            userEntity = user.fromDto(),
            now = LocalDateTime.now()
        )
        return existsByUserEntityAndEndDateIsAfter
    }
}