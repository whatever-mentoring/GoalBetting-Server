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
        predictionType: PredictionType
    ): Betting {
        val betting = bettingRepository.save(
            BettingEntity(
                userEntity = userRepository.findById(userId).get(),
                goalEntity = goalRepository.findById(goalId).get(),
                predictionType = predictionType
            )
        )
        return betting.toDto()
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
    fun softDelete(bettingId: Long) {
        val betting = bettingRepository.findByIdOrNull(bettingId)
            ?: throw IllegalStateException("Cannot find betting $bettingId")
        betting.deletedAt = LocalDateTime.now()
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

    fun findByIdOrNull(bettingId: Long): Betting? {
        return bettingRepository.findByIdOrNull(bettingId)?.toDto()
    }
}