package com.whatever.raisedragon.applicationservice

import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode
import com.whatever.raisedragon.controller.goal.GoalBettingHost
import com.whatever.raisedragon.controller.goal.GoalBettingParticipant
import com.whatever.raisedragon.controller.goal.GoalResponse
import com.whatever.raisedragon.controller.goal.GoalRetrieveParticipantResponse
import com.whatever.raisedragon.domain.betting.BettingService
import com.whatever.raisedragon.domain.goal.*
import com.whatever.raisedragon.domain.user.UserService
import com.whatever.raisedragon.domain.user.fromDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional(readOnly = true)
@Service
class GoalApplicationService(
    private val goalService: GoalService,
    private val userService: UserService,
    private val bettingService: BettingService
) {

    @Transactional
    fun createGoal(
        content: Content,
        bettingType: BettingType,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        userId: Long,
    ): GoalResponse {
        if (isNumberOfGoalUnderOneHundred(userId)) throw BaseException.of(
            exceptionCode = ExceptionCode.E409_CONFLICT,
            executionMessage = "다짐을 생성하는 중, 생성할 수 있는 다짐 갯수를 초과하였습니다."
        )
        val goal = goalService.create(
            userId = userId,
            content = content,
            bettingType = bettingType,
            threshold = Threshold(0),
            startDate = startDate,
            endDate = endDate
        )
        return GoalResponse(
            id = goal.id,
            type = goal.type,
            content = goal.content,
            threshold = goal.threshold,
            startDate = goal.startDate,
            endDate = goal.endDate
        )
    }

    fun retrieveGoal(goalId: Long): GoalResponse {
        val goal = goalService.loadById(goalId)
        return GoalResponse(
            id = goal.id,
            type = goal.type,
            content = goal.content,
            threshold = goal.threshold,
            startDate = goal.startDate,
            endDate = goal.endDate
        )
    }

    fun retrieveAllByUserId(userId: Long): List<GoalResponse> {
        val goals = goalService.loadAllByUserId(userId)
        val response: MutableList<GoalResponse> = mutableListOf()

        goals.map {
            val goalResponse = GoalResponse(
                id = it.id,
                type = it.type,
                content = it.content,
                threshold = it.threshold,
                startDate = it.startDate,
                endDate = it.endDate
            )
            response.add(goalResponse)
        }
        return response
    }

    fun retrieveGoalBettingParticipant(
        userId: Long,
        goalId: Long
    ): GoalRetrieveParticipantResponse {
        val hostUser = userService.loadById(userId)
        val goal = goalService.loadById(goalId)
        val bettingList = bettingService.loadAllByGoalId(goalId)

        val hostDto = GoalBettingHost(
            hostUserId = userId,
            hostUserNickname = hostUser.nickname.value,
            goalCreatedAt = goal.createdAt
        )

        val participants = bettingList.map {
            GoalBettingParticipant(
                bettingId = it.id,
                bettingPredictionType = it.predictionType,
                bettingResult = it.result,
                bettingCreatedAt = it.createdAt!!
            )
        }

        return GoalRetrieveParticipantResponse(
            hostUser = hostDto,
            participants = participants
        )
    }

    @Transactional
    fun modifyGoal(
        userId: Long,
        goalId: Long,
        content: String
    ): GoalResponse {
        val goal = goalService.loadById(goalId)
        isNotUsersGoal(goal, userId)
        isAlreadyStarted(goal)

        val modifiedGoal = goalService.modify(
            goal = goal,
            userEntity = userService.loadById(userId).fromDto(),
            content = content
        )

        return GoalResponse(
            id = modifiedGoal.id,
            type = modifiedGoal.type,
            content = modifiedGoal.content,
            threshold = modifiedGoal.threshold,
            startDate = modifiedGoal.startDate,
            endDate = modifiedGoal.endDate
        )
    }

    @Transactional
    fun deleteGoal(
        userId: Long,
        goalId: Long,
    ) {
        val goal = goalService.loadById(goalId)
        isNotUsersGoal(goal, userId)
        isAlreadyStarted(goal)

        goalService.delete(
            goal = goal,
            userEntity = userService.loadById(userId).fromDto(),
        )
    }

    private fun isNotUsersGoal(goal: Goal, userId: Long) {
        if (goal.userId != userId) {
            throw BaseException.of(
                exceptionCode = ExceptionCode.E400_BAD_REQUEST,
                executionMessage = "다짐을 수정하는 중, 쓰기 권한이 없는 리소스에 대한 요청은 수행할 수 없습니다."
            )
        }
    }

    private fun isAlreadyStarted(goal: Goal) {
        if (goal.startDate < LocalDateTime.now()) {
            throw BaseException.of(
                exceptionCode = ExceptionCode.E400_BAD_REQUEST,
                executionMessage = "다짐에 쓰기작업을 하는 중, 이미 시작된 다짐은 수정/삭제할 수 없습니다."
            )
        }
    }

    private fun isNumberOfGoalUnderOneHundred(userId: Long): Boolean {
        return goalService.loadAllByUserId(userId).size > 99
    }
}