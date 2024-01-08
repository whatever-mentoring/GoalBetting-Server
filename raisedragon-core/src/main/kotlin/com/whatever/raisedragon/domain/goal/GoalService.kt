package com.whatever.raisedragon.domain.goal

import com.whatever.raisedragon.domain.user.User
import com.whatever.raisedragon.domain.user.UserEntity
import com.whatever.raisedragon.domain.user.UserRepository
import com.whatever.raisedragon.domain.user.fromDto
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional(readOnly = true)
@Service
class GoalService(
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
                userEntity = userRepository.findById(userId).get(),
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
        return goalRepository.findByIdOrNull(id)?.toDto()
            ?: throw IllegalArgumentException("다짐을 조회하는 중, 잘못된 내용을 요청하셨습니다.")
    }

    fun existsByUserIdAndAnyResult(userId: Long, goalResult: GoalResult): Boolean {
        val userEntity = userRepository.findByIdOrNull(userId)
            ?: throw IllegalArgumentException("cannot find user $userId")
        return goalRepository.findAllByUserEntityAndGoalResult(userEntity, goalResult).isNotEmpty()
    }

    fun loadAllByUserId(userId: Long): List<Goal> {
        return goalRepository.findAllByUserEntity(
            userRepository.findById(userId).get()
        ).map { it.toDto() }
    }

    fun findAllByIds(goalIds: Set<Long>): List<Goal> {
        return goalRepository.findAllById(goalIds).map { it.toDto() }
    }

    fun findAllByEndDateLessThanEqualAndResultIsProceeding(endDate: LocalDateTime): List<Goal> {
        return goalRepository.findAllByEndDateLessThanEqualAndGoalResultIs(endDate, GoalResult.PROCEEDING).map { it.toDto() }
    }

    @Transactional
    fun updateResult(goalId: Long, goalResult: GoalResult): Goal {
        val goalEntity =
            goalRepository.findByIdOrNull(goalId) ?: throw IllegalStateException("cannot find goal $goalId")
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
            userEntity = userRepository.findByIdOrNull(userId) ?: throw IllegalArgumentException("Cannot find user $userId")
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