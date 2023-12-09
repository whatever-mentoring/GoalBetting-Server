package com.whatever.raisedragon.applicationservice

import com.whatever.raisedragon.controller.betting.BettingCreateUpdateResponse
import com.whatever.raisedragon.controller.betting.BettingRetrieveResponse
import com.whatever.raisedragon.domain.betting.BettingService
import com.whatever.raisedragon.domain.betting.PredictionType
import com.whatever.raisedragon.domain.goal.GoalService
import com.whatever.raisedragon.domain.user.UserService
import com.whatever.raisedragon.security.authentication.UserInfo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BettingApplicationService(
    private val bettingService: BettingService,
    private val userService: UserService,
    private val goalService: GoalService
) {

    fun create(
        userId: Long,
        goalId: Long,
        predictionType: PredictionType
    ): BettingCreateUpdateResponse {

        val betting = bettingService.create(
            user = userService.loadById(userId),
            goal = goalService.loadById(goalId),
            predictionType = predictionType
        )

        return BettingCreateUpdateResponse(
            BettingRetrieveResponse(
                id = betting.id,
                userId = betting.userId,
                goalId = betting.goalId,
                predictionType = predictionType
            )
        )
    }

    fun retrieve(): BettingRetrieveResponse {
        return BettingRetrieveResponse(
            id = 0L,
            userId = 0L,
            goalId = 0L,
            predictionType = PredictionType.SUCCESS,
        )
    }
}