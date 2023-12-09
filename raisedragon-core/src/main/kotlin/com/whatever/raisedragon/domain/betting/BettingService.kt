package com.whatever.raisedragon.domain.betting

import com.whatever.raisedragon.domain.goal.Goal
import com.whatever.raisedragon.domain.goal.fromDto
import com.whatever.raisedragon.domain.user.User
import com.whatever.raisedragon.domain.user.fromDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BettingService(
    private val bettingRepository: BettingRepository
) {
    fun create(
        user: User,
        goal: Goal,
        predictionType: PredictionType
    ): Betting {
        val betting = bettingRepository.save(
            BettingEntity(
                userEntity = user.fromDto(),
                goalEntity = goal.fromDto(),
                predictionType = predictionType
            )
        )
        return betting.toDto()
    }
}