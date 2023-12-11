package com.whatever.raisedragon.applicationservice

import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode
import com.whatever.raisedragon.domain.goal.*
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
    ): Goal {
        if (isNumberOfGoalUnderOneHundred(userId)) throw BaseException.of(
            exceptionCode = ExceptionCode.E409_CONFLICT,
            executionMessage = "다짐을 생성하는 중, 생성할 수 있는 다짐 갯수를 초과하였습니다."
        )
        return goalService.create(
            userId = userId,
            content = content,
            bettingType = bettingType,
            threshold = threshold,
            startDate = startDate,
            endDate = endDate
        )
    }


    private fun isNumberOfGoalUnderOneHundred(userId: Long): Boolean {
        return goalService.loadAllByUserId(userId).size < 100
    }
}