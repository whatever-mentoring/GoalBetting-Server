package com.whatever.raisedragon.applicationservice.betting

import com.whatever.raisedragon.applicationservice.betting.dto.BettingCreateServiceRequest
import com.whatever.raisedragon.applicationservice.betting.dto.BettingCreateUpdateResponse
import com.whatever.raisedragon.applicationservice.betting.dto.BettingRetrieveResponse
import com.whatever.raisedragon.applicationservice.betting.dto.BettingUpdateServiceRequest
import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode
import com.whatever.raisedragon.domain.betting.Betting
import com.whatever.raisedragon.domain.betting.BettingService
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
    fun create(request: BettingCreateServiceRequest): BettingCreateUpdateResponse {
        if (canBet(request.userId, request.goalId)) {
            val betting = bettingService.create(
                userId = request.userId,
                goalId = request.goalId,
                bettingPredictionType = request.bettingPredictionType
            )

            return BettingCreateUpdateResponse(
                BettingRetrieveResponse(
                    id = betting.id,
                    userId = betting.userId,
                    goalId = betting.goalId,
                    bettingPredictionType = betting.bettingPredictionType,
                    bettingResult = betting.bettingResult
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
    fun update(request: BettingUpdateServiceRequest): BettingRetrieveResponse {
        val betting = findByIdOrThrowException(request.bettingId)
        betting.validateOwnerId(request.userId)
        betting.validateStartDate()
        return BettingRetrieveResponse.of(bettingService.updatePredictionType(request.bettingId,
            request.bettingPredictionType))
    }

    @Transactional
    fun delete(userId: Long, bettingId: Long) {
        val betting = findByIdOrThrowException(bettingId)
        betting.validateOwnerId(userId)
        betting.validateStartDate()
        bettingService.hardDelete(bettingId)
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
        val now = LocalDateTime.now()
        val goalStartDate = goalService.findById(goalId).startDate
        if (now.toLocalDate() >= goalStartDate.toLocalDate()) {
            throw BaseException.of(ExceptionCode.E400_BAD_REQUEST, "이미 시작한 내기 입니다.")
        }
    }

    private fun canBet(
        userId: Long,
        goalId: Long
    ) = bettingService.loadUserAndGoal(userId, goalId) == null
}