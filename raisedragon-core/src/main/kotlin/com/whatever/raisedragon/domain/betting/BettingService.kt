package com.whatever.raisedragon.domain.betting

import com.whatever.raisedragon.domain.goal.GoalRepository
import com.whatever.raisedragon.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BettingService(
    private val bettingRepository: BettingRepository,
    private val userRepository: UserRepository,
    private val goalRepository: GoalRepository
) {
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

    @Transactional(readOnly = true)
    fun loadUserAndGoal(
        userId: Long,
        goalId: Long
    ): BettingEntity? {
        return bettingRepository.findByUserEntityAndGoalEntity(
            userEntity = userRepository.findById(userId).get(),
            goalEntity = goalRepository.findById(goalId).get()
        )
    }
}