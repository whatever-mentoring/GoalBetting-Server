package com.whatever.raisedragon.applicationservice

import com.whatever.raisedragon.controller.betting.BettingCreateUpdateResponse
import com.whatever.raisedragon.controller.betting.BettingRetrieveResponse
import com.whatever.raisedragon.domain.betting.BettingService
import com.whatever.raisedragon.domain.betting.PredictionType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BettingApplicationService(
    private val bettingService: BettingService
) {

    fun create(): BettingCreateUpdateResponse {
        return BettingCreateUpdateResponse(
            BettingRetrieveResponse(
                id = 0L,
                userId = 0L,
                goalId = 0L,
                predictionType = PredictionType.SUCCESS,
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