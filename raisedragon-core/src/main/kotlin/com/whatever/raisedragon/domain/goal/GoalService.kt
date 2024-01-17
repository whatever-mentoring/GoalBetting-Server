package com.whatever.raisedragon.domain.goal

import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.domain.user.UserRepository
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

    fun findById(id: Long): Goal {
        return goalRepository.findById(id).orElseThrow(notFoundExceptionSupplier).toDto()
    }

    fun findAllByUserId(userId: Long): List<Goal> {
        return goalRepository.findAllByUserEntity(
            userRepository.findById(userId).orElseThrow(notFoundExceptionSupplier)
        ).map { it.toDto() }
    }

    fun findAllByIds(goalIds: Set<Long>): List<Goal> {
        return goalRepository.findAllById(goalIds).map { it.toDto() }
    }

    fun findAllByEndDateLessThanEqualAndGoalResultIsProceeding(endDate: LocalDateTime): List<Goal> {
        return goalRepository.findAllByEndDateLessThanEqualAndGoalResultIs(endDate, GoalResult.PROCEEDING)
            .map { it.toDto() }
    }

    fun existsByUserIdAndAnyGoalResult(userId: Long, goalResult: GoalResult): Boolean {
        val userEntity = userRepository.findById(userId).orElseThrow(notFoundExceptionSupplier)
        return goalRepository.findAllByUserEntityAndGoalResult(userEntity, goalResult).isNotEmpty()
    }

    fun existsByUserAndEndDateIsAfterThanNow(userId: Long): Boolean {
        return goalRepository.existsByUserEntityAndEndDateIsAfter(
            userEntity = userRepository.findById(userId).orElseThrow(notFoundExceptionSupplier),
            now = LocalDateTime.now()
        )
    }

    @Transactional
    fun updateResult(goalId: Long, goalResult: GoalResult): Goal {
        val goalEntity = goalRepository.findById(goalId).orElseThrow(notFoundExceptionSupplier)
        goalEntity.goalResult = goalResult
        return goalEntity.toDto()
    }

    @Transactional
    fun updateContent(goalId: Long, content: Content): Goal {
        val goalEntity = goalRepository.findById(goalId).orElseThrow(notFoundExceptionSupplier)
        goalEntity.content = content
        return goalEntity.toDto()
    }

    @Transactional
    fun increaseThreshold(id: Long) {
        val goalEntity = goalRepository.findById(id).orElseThrow(notFoundExceptionSupplier)
        goalEntity.threshold = Threshold(goalEntity.threshold.value + 1)
    }

    @Transactional
    fun softDelete(id: Long): Goal {
        val goalEntity = goalRepository.findById(id).orElseThrow(notFoundExceptionSupplier)
        goalEntity.deletedAt = LocalDateTime.now()
        return goalEntity.toDto()
    }

    @Transactional
    fun hardDeleteByUserId(userId: Long) {
        val goals = goalRepository.findAllByUserEntity(
            userEntity = userRepository.findById(userId).orElseThrow(notFoundExceptionSupplier)
        )
        goalRepository.deleteAll(goals)
    }
}