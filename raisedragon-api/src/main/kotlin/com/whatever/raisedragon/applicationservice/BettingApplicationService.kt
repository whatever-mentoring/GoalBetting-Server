package com.whatever.raisedragon.applicationservice

import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode
import com.whatever.raisedragon.controller.betting.BettingCreateUpdateResponse
import com.whatever.raisedragon.controller.betting.BettingRetrieveResponse
import com.whatever.raisedragon.domain.betting.BettingService
import com.whatever.raisedragon.domain.betting.PredictionType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BettingApplicationService(
    private val bettingService: BettingService,
) {

    fun create(
        userId: Long,
        goalId: Long,
        predictionType: PredictionType
    ): BettingCreateUpdateResponse {
        if (canBet(userId, goalId)) {
            val betting = bettingService.create(
                userId = userId,
                goalId = goalId,
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

        throw BaseException.of(
            exceptionCode = ExceptionCode.E409_CONFLICT,
            executionMessage = "이미 배팅에 참여한 다짐입니다."
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

    private fun canBet(
        userId: Long,
        goalId: Long
    ) = bettingService.loadUserAndGoal(userId, goalId) == null
}