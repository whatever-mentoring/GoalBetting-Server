package com.whatever.raisedragon.applicationservice

import com.whatever.raisedragon.applicationservice.dto.BettingResponse
import com.whatever.raisedragon.applicationservice.dto.UserResponse
import com.whatever.raisedragon.controller.betting.BettingCreateUpdateResponse
import com.whatever.raisedragon.controller.betting.BettingRetrieveResponse
import com.whatever.raisedragon.domain.betting.Betting
import com.whatever.raisedragon.domain.betting.BettingService
import com.whatever.raisedragon.domain.betting.PredictionType
import com.whatever.raisedragon.domain.betting.Result
import com.whatever.raisedragon.domain.goal.BettingType
import com.whatever.raisedragon.domain.goal.Content
import com.whatever.raisedragon.domain.goal.Goal
import com.whatever.raisedragon.domain.goal.Threshold
import com.whatever.raisedragon.domain.user.Nickname
import com.whatever.raisedragon.domain.user.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class BettingApplicationService(
    private val bettingService: BettingService
) {

    fun create(): BettingCreateUpdateResponse {
        return BettingCreateUpdateResponse(
            BettingResponse.of(
                Betting(
                    id = 0L,
                    user = User(
                        id = 0L,
                        oauthTokenPayload = "sample",
                        fcmTokenPayload = "sample",
                        nickname = Nickname("sample"),
                        deletedAt = null,
                        createdAt = null,
                        updatedAt = null
                    ),
                    goal = Goal(
                        id = 1L,
                        type = BettingType.BILLING,
                        content = Content("sample"),
                        threshold = Threshold(0),
                        startDate = LocalDateTime.now().plusDays(1),
                        endDate = LocalDateTime.now().plusMonths(1),
                        createdAt = LocalDateTime.now(),
                        updatedAt = LocalDateTime.now(),
                        deletedAt = null
                    ),
                    predictionType = PredictionType.SUCCESS,
                    result = Result.PROCEEDING,
                    deletedAt = null,
                    createdAt = null,
                    updatedAt = null
                )
            )
        )
    }

    fun retrieve(): BettingRetrieveResponse {
        return BettingRetrieveResponse(
            user = UserResponse.of(
                User(
                    id = 0L,
                    oauthTokenPayload = "sample",
                    fcmTokenPayload = "sample",
                    nickname = Nickname("sample"),
                    deletedAt = null,
                    createdAt = null,
                    updatedAt = null
                )
            ),
            goal = Goal(
                id = 1L,
                type = BettingType.BILLING,
                content = Content("sample"),
                threshold = Threshold(0),
                startDate = LocalDateTime.now().plusDays(1),
                endDate = LocalDateTime.now().plusMonths(1),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                deletedAt = null
            ),
            predictionType = PredictionType.SUCCESS,
            result = Result.PROCEEDING
        )
    }
}