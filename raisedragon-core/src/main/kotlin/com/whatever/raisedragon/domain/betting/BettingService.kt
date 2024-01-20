package com.whatever.raisedragon.domain.betting

import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.domain.goal.GoalRepository
import com.whatever.raisedragon.domain.user.UserRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.function.Supplier

@Service
@Transactional(readOnly = true)
class BettingService(
    @Qualifier("notFoundExceptionSupplier") private val notFoundExceptionSupplier: Supplier<BaseException>,
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
                userEntity = userRepository.findById(userId)
                    .orElseThrow(notFoundExceptionSupplier),
                goalEntity = goalRepository.findById(goalId)
                    .orElseThrow(notFoundExceptionSupplier),
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
            userEntity = userRepository.findById(userId).orElseThrow(notFoundExceptionSupplier),
            goalEntity = goalRepository.findById(goalId).orElseThrow(notFoundExceptionSupplier)
        )?.toDto()
    }

    fun findAllByGoalId(
        goalId: Long
    ): List<Betting> {
        return bettingRepository.findAllByGoalEntity(
            goalEntity = goalRepository.findById(goalId)
                .orElseThrow(notFoundExceptionSupplier)
        ).map { it.toDto() }
    }

    fun findByIdOrNull(bettingId: Long): Betting? {
        return bettingRepository.findByIdOrNull(bettingId)?.toDto()
    }

    fun findAllByUserId(userId: Long): List<Betting> {
        val userEntity = userRepository.findById(userId).orElseThrow(notFoundExceptionSupplier)
        return bettingRepository.findAllByUserEntity(userEntity).map { it.toDto() }
    }

    fun existsBettingParticipantUser(userId: Long): Boolean {
        val userEntity = userRepository.findById(userId).orElseThrow(notFoundExceptionSupplier)
        val bettings = bettingRepository.findAllByUserEntity(userEntity)

        for (betting in bettings) {
            if (betting.goalEntity.endDate > LocalDateTime.now()) return true
        }
        return false
    }

    @Transactional
    fun updatePredictionType(bettingId: Long, bettingPredictionType: BettingPredictionType): Betting {
        val betting = bettingRepository.findById(bettingId).orElseThrow(notFoundExceptionSupplier)
        if (betting.bettingPredictionType != bettingPredictionType) {
            betting.bettingPredictionType = bettingPredictionType
        }
        return betting.toDto()
    }

    @Transactional
    fun updateResult(bettingId: Long, bettingResult: BettingResult): Betting {
        val betting = bettingRepository.findById(bettingId).orElseThrow(notFoundExceptionSupplier)
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
    fun hardDelete(bettingId: Long) {
        val betting = bettingRepository.findById(bettingId).orElseThrow(notFoundExceptionSupplier)
        bettingRepository.delete(betting)
    }

    @Transactional
    fun hardDeleteByUserId(userId: Long) {
        val bettingEntities = bettingRepository.findAllByUserEntity(
            userEntity = userRepository.findById(userId).orElseThrow(notFoundExceptionSupplier)
        )
        bettingRepository.deleteAll(bettingEntities)
    }
}