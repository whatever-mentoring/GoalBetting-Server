package com.whatever.raisedragon.domain.betting

import com.whatever.raisedragon.domain.goal.GoalRepository
import com.whatever.raisedragon.domain.user.User
import com.whatever.raisedragon.domain.user.UserRepository
import com.whatever.raisedragon.domain.user.fromDto
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class BettingService(
    private val bettingRepository: BettingRepository,
    private val userRepository: UserRepository,
    private val goalRepository: GoalRepository,
) {
    @Transactional
    fun create(
        userId: Long,
        goalId: Long,
        predictionType: PredictionType
    ): Betting {
        val betting = bettingRepository.save(
            BettingEntity(
                userEntity = userRepository.findById(userId).get(),
                goalEntity = goalRepository.findById(goalId).get(),
                predictionType = predictionType,
                result = Result.PROCEEDING
            )
        )
        return betting.toDto()
    }

    fun loadUserAndGoal(
        userId: Long,
        goalId: Long
    ): Betting? {
        return bettingRepository.findByUserEntityAndGoalEntity(
            userEntity = userRepository.findById(userId).get(),
            goalEntity = goalRepository.findById(goalId).get()
        )?.toDto()
    }

    fun findAllByGoalIdAndNotDeleted(goalId: Long): List<Betting> {
        return bettingRepository.findAllByGoalEntityAndDeletedAtIsNull(
            goalEntity = goalRepository.findByIdOrNull(goalId)
                ?: throw IllegalStateException("cannot find goal $goalId")
        ).map { it.toDto() }
    }

    fun loadAllByGoalId(
        goalId: Long
    ): List<Betting> {
        return bettingRepository.findAllByGoalEntity(
            goalEntity = goalRepository.findById(goalId).get()
        ).map { it.toDto() }
    }

    fun findByIdOrNull(bettingId: Long): Betting? {
        return bettingRepository.findByIdOrNull(bettingId)?.toDto()
    }

    fun findAllByUserId(userId: Long): List<Betting> {
        val userEntity =
            userRepository.findByIdOrNull(userId) ?: throw IllegalStateException("cannot find user $userId")
        return bettingRepository.findAllByUserEntity(userEntity).map { it.toDto() }
    }

    @Transactional
    fun update(bettingId: Long, predictionType: PredictionType): Betting {
        val betting = bettingRepository.findByIdOrNull(bettingId)
            ?: throw IllegalStateException("Cannot find betting $bettingId")
        if (betting.predictionType != predictionType) {
            betting.predictionType = predictionType
        }
        return betting.toDto()
    }

    @Transactional
    fun updateResult(bettingId: Long, result: Result): Betting {
        val betting = bettingRepository.findByIdOrNull(bettingId)
            ?: throw IllegalStateException("Cannot find betting $bettingId")
        if (betting.result != result) {
            betting.result = result
        }
        return betting.toDto()
    }

    @Transactional
    fun bulkModifyingByResultWhereIdInIds(bettingIds: Set<Long>, result: Result): Int {
        return bettingRepository.bulkModifyingByResultWhereIdInIds(result, bettingIds)
    }

    @Transactional
    fun softDelete(bettingId: Long) {
        val betting = bettingRepository.findByIdOrNull(bettingId)
            ?: throw IllegalStateException("Cannot find betting $bettingId")
        betting.deletedAt = LocalDateTime.now()
    }

    @Transactional
    fun hardDelete(user: User) {
        val bettings = bettingRepository.findAllByUserEntity(user.fromDto())
        bettingRepository.deleteAll(bettings)
    }
}