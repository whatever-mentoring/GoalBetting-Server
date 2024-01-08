package com.whatever.raisedragon.domain.betting

import com.whatever.raisedragon.domain.goal.GoalRepository
import com.whatever.raisedragon.domain.user.UserRepository
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
        bettingPredictionType: BettingPredictionType
    ): Betting {
        val betting = bettingRepository.save(
            BettingEntity(
                userEntity = userRepository.findById(userId).get(),
                goalEntity = goalRepository.findById(goalId).get(),
                bettingPredictionType = bettingPredictionType,
                bettingResult = BettingResult.PROCEEDING
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
        return bettingRepository.findAllByGoalEntity(
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

    fun existsBettingParticipantUser(userId: Long): Boolean {
        val userEntity =
            userRepository.findByIdOrNull(userId) ?: throw IllegalStateException("cannot find user $userId")
        val bettings = bettingRepository.findAllByUserEntity(userEntity)

        for (betting in bettings) {
            if (betting.goalEntity.endDate > LocalDateTime.now()) return true
        }
        return false
    }

    @Transactional
    fun update(bettingId: Long, bettingPredictionType: BettingPredictionType): Betting {
        val betting = bettingRepository.findByIdOrNull(bettingId)
            ?: throw IllegalStateException("Cannot find betting $bettingId")
        if (betting.bettingPredictionType != bettingPredictionType) {
            betting.bettingPredictionType = bettingPredictionType
        }
        return betting.toDto()
    }

    @Transactional
    fun updateResult(bettingId: Long, bettingResult: BettingResult): Betting {
        val betting = bettingRepository.findByIdOrNull(bettingId)
            ?: throw IllegalStateException("Cannot find betting $bettingId")
        if (betting.bettingResult != bettingResult) {
            betting.bettingResult = bettingResult
        }
        return betting.toDto()
    }

    @Transactional
    fun bulkModifyingByResultWhereIdInIds(bettingIds: Set<Long>, bettingResult: BettingResult): Int {
        return bettingRepository.bulkModifyingByBettingResultWhereIdInIds(bettingResult, bettingIds)
    }

    @Transactional
    fun softDelete(bettingId: Long) {
        val betting = bettingRepository.findByIdOrNull(bettingId)
            ?: throw IllegalStateException("Cannot find betting $bettingId")
        betting.deletedAt = LocalDateTime.now()
    }

    @Transactional
    fun hardDelete(bettingId: Long) {
        val betting = bettingRepository.findByIdOrNull(bettingId)
            ?: throw IllegalStateException("Cannot find betting $bettingId")
        bettingRepository.delete(betting)
    }

    @Transactional
    fun hardDeleteByUserId(userId: Long) {
        val bettings = bettingRepository.findAllByUserEntity(
            userEntity = userRepository.findByIdOrNull(userId) ?: throw IllegalArgumentException("Cannot find user $userId")
        )
        bettingRepository.deleteAll(bettings)
    }
}