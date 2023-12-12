package com.whatever.raisedragon.applicationservice

import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode
import com.whatever.raisedragon.controller.goal.GoalResponse
import com.whatever.raisedragon.domain.goal.BettingType
import com.whatever.raisedragon.domain.goal.Content
import com.whatever.raisedragon.domain.goal.GoalService
import com.whatever.raisedragon.domain.goal.Threshold
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional(readOnly = true)
@Service
class GoalApplicationService(
    private val goalService: GoalService,
) {

    @Transactional
    fun createGoal(
        content: Content,
        bettingType: BettingType,
        threshold: Threshold,
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
            threshold = threshold,
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


    private fun isNumberOfGoalUnderOneHundred(userId: Long): Boolean {
        return goalService.loadAllByUserId(userId).size < 100
    }
}