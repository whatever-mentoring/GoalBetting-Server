package com.whatever.raisedragon.applicationservice.goalproof

import com.whatever.raisedragon.applicationservice.goalproof.dto.*
import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode
import com.whatever.raisedragon.domain.gifticon.URL
import com.whatever.raisedragon.domain.goal.Goal
import com.whatever.raisedragon.domain.goal.GoalService
import com.whatever.raisedragon.domain.goalproof.Comment
import com.whatever.raisedragon.domain.goalproof.GoalProof
import com.whatever.raisedragon.domain.goalproof.GoalProofService
import com.whatever.raisedragon.domain.user.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
@Transactional(readOnly = true)
class GoalProofApplicationService(
    private val goalProofService: GoalProofService,
    private val goalService: GoalService,
    private val userService: UserService,
) {

    @Transactional
    fun create(request: GoalProofCreateServiceRequest): GoalProofCreateUpdateResponse {
        val goal = goalService.findById(request.goalId)

        isGoalProofAlreadyExists(goal.id)
        validateGoalProofCreateTiming(goal)

        val goalProof = goalProofService.create(
            userId = request.userId,
            goalId = goal.id,
            url = URL(request.url),
            comment = request.comment
        )
        goalService.increaseThreshold(goal.id)
        return GoalProofCreateUpdateResponse(GoalProofRetrieveResponse.of(goalProof))
    }

    private fun isGoalProofAlreadyExists(goalId: Long) {
        if (
            goalProofService.existsGoalIdAndDateTimeBetween(
                goalId = goalId,
                targetDateTime = LocalDateTime.now()
            )
        ) {
            throw BaseException.of(
                exceptionCode = ExceptionCode.E409_CONFLICT,
                executionMessage = "해당 날짜에 대한 인증은 이미 생성되어있습니다."
            )
        }
    }

    fun retrieve(goalProofId: Long): GoalProofRetrieveResponse {
        return GoalProofRetrieveResponse.of(findByIdOrThrowException(goalProofId))
    }

    fun retrieveAll(goalId: Long, userId: Long): GoalProofListRetrieveResponse {
        val goalProofs = goalProofService.findAllByGoalIdAndUserId(goalId, userId)
        val goalStartDateTime = goalService.findById(goalId).startDate

        val progressDays = goalProofs.map {
            it.createdAt!!.dayOfMonth.minus(goalStartDateTime.dayOfMonth) + 1
        }

        val goalProofRetrieveResponses = goalProofs.map {
            GoalProofRetrieveResponse.of(it)
        }

        return GoalProofListRetrieveResponse(
            goalProofs = goalProofRetrieveResponses,
            progressDays = progressDays
        )
    }

    fun isSuccess(goalId: Long, userId: Long): Boolean {
        val goalProofs = goalProofService.findAllByGoalIdAndUserId(goalId, userId)
        // TODO : Number 7 must be changed after adjusting goal's threshold
        return goalProofs.size >= 7
    }

    @Transactional
    fun update(request: GoalProofUpdateServiceRequest): GoalProofRetrieveResponse {
        validateUpdatable(request.url, request.comment)
        findByIdOrThrowException(request.goalProofId)
            .also { it.validateOwnerId(request.userId) }
            .also { it.validateEndDate() }
        return GoalProofRetrieveResponse.of(
            goalProofService.update(request.goalProofId, request.url?.let { URL(it) }, request.comment)
        )
    }

    private fun validateGoalProofCreateTiming(goal: Goal) {
        val now = LocalDateTime.now()
        val daysBetween = ChronoUnit.DAYS.between(goal.startDate, now) + 1


        if (1 > daysBetween || daysBetween > 7) {
            throw BaseException.of(
                exceptionCode = ExceptionCode.E400_BAD_REQUEST,
                executionMessage = "인증 날짜가 올바르지 않습니다."
            )
        }
    }

    private fun findByIdOrThrowException(goalProofId: Long): GoalProof {
        return goalProofService.findById(goalProofId) ?: throw BaseException.of(ExceptionCode.E404_NOT_FOUND)
    }

    private fun validateUpdatable(url: String?, comment: Comment?) {
        if (url == null && comment == null) {
            throw BaseException.of(ExceptionCode.E400_BAD_REQUEST)
        }
    }

    private fun GoalProof.validateOwnerId(userId: Long) {
        if (this.userId != userId) {
            throw BaseException.of(ExceptionCode.E403_FORBIDDEN, "접근할 수 없는 다짐 인증입니다")
        }
    }

    private fun GoalProof.validateEndDate() {
        if (goalService.findById(goalId).endDate > LocalDateTime.now()) {
            throw BaseException.of(ExceptionCode.E400_BAD_REQUEST, "이미 끝난 내기입니다")
        }
    }
}