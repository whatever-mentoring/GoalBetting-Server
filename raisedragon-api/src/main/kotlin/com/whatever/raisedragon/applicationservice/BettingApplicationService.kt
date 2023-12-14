package com.whatever.raisedragon.applicationservice

import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode
import com.whatever.raisedragon.controller.betting.BettingCreateUpdateResponse
import com.whatever.raisedragon.controller.betting.BettingRetrieveResponse
import com.whatever.raisedragon.domain.betting.Betting
import com.whatever.raisedragon.domain.betting.BettingService
import com.whatever.raisedragon.domain.betting.PredictionType
import com.whatever.raisedragon.domain.goal.GoalService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class BettingApplicationService(
    private val goalService: GoalService,
    private val bettingService: BettingService,
) {

    @Transactional
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
                    predictionType = betting.predictionType,
                    result = betting.result
                )
            )
        }

        throw BaseException.of(
            exceptionCode = ExceptionCode.E409_CONFLICT,
            executionMessage = "이미 배팅에 참여한 다짐입니다."
        )
    }

    fun retrieve(bettingId: Long): BettingRetrieveResponse {
        return BettingRetrieveResponse.of(findByIdOrThrowException(bettingId))
    }

    @Transactional
    fun update(userId: Long, bettingId: Long, predictionType: PredictionType): BettingRetrieveResponse {
        val betting = findByIdOrThrowException(bettingId)
        betting.validateOwnerId(userId)
        betting.validateStartDate()
        return BettingRetrieveResponse.of(bettingService.update(bettingId, predictionType))
    }

    @Transactional
    fun delete(userId: Long, bettingId: Long) {
        val betting = findByIdOrThrowException(bettingId)
        betting.validateOwnerId(userId)
        betting.validateStartDate()
        bettingService.softDelete(bettingId)
    }

    private fun findByIdOrThrowException(bettingId: Long): Betting {
        return bettingService.findByIdOrNull(bettingId) ?: throw BaseException.of(
            ExceptionCode.E404_NOT_FOUND,
            "베팅을 찾을 수 없습니다"
        )
    }

    private fun Betting.validateOwnerId(userId: Long) {
        if (this.userId != userId) {
            throw BaseException.of(ExceptionCode.E403_FORBIDDEN, "접근할 수 없는 내기 입니다.")
        }
    }

    private fun Betting.validateStartDate() {
        if (goalService.loadById(goalId).startDate > LocalDateTime.now()) {
            throw BaseException.of(ExceptionCode.E400_BAD_REQUEST, "이미 시작한 내기 입니다.")
        }
    }

    private fun canBet(
        userId: Long,
        goalId: Long
    ) = bettingService.loadUserAndGoal(userId, goalId) == null
}