package com.whatever.raisedragon.applicationservice.goalgifticon

import com.whatever.raisedragon.applicationservice.goalgifticon.dto.GifticonResponse
import com.whatever.raisedragon.applicationservice.goalgifticon.dto.GoalGifticonCreateServiceRequest
import com.whatever.raisedragon.applicationservice.goalgifticon.dto.GoalGifticonResponse
import com.whatever.raisedragon.applicationservice.goalgifticon.dto.GoalGifticonUpdateServiceRequest
import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode
import com.whatever.raisedragon.domain.gifticon.GifticonService
import com.whatever.raisedragon.domain.gifticon.URL
import com.whatever.raisedragon.domain.goal.*
import com.whatever.raisedragon.domain.goalgifticon.GoalGifticonService
import com.whatever.raisedragon.domain.user.UserService
import com.whatever.raisedragon.domain.user.fromDto
import com.whatever.raisedragon.domain.winner.WinnerService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional(readOnly = true)
@Service
class GoalGifticonApplicationService(
    private val gifticonService: GifticonService,
    private val goalService: GoalService,
    private val goalGifticonService: GoalGifticonService,
    private val userService: UserService,
    private val winnerService: WinnerService
) {

    @Transactional
    fun createAndUploadGifticon(request: GoalGifticonCreateServiceRequest): GoalGifticonResponse {
        val goal = goalService.findById(request.goalId)
        if (isNotBettingTypeBilling(goal.type)) throw BaseException.of(
            exceptionCode = ExceptionCode.E400_BAD_REQUEST,
            executionMessage = "기프티콘을 업르도하는 중, 무료 다짐에는 기프티콘을 업로드할 수 없습니다."
        )

        if (isBrokenUserGoal(goal, request.userId)) throw BaseException.of(
            exceptionCode = ExceptionCode.E400_BAD_REQUEST,
            executionMessage = "기프티콘을 업로드하는 중, 요청한 유저가 생성한 다짐에 대한 요청이 아닙니다."
        )

        if (isBrokenTiming(goal)) throw BaseException.of(
            exceptionCode = ExceptionCode.E400_BAD_REQUEST,
            executionMessage = "기프티콘을 업로드하는 중, 이미 다짐 수행이 시작되어 업로드할 수 없습니다."
        )

        val gifticon = gifticonService.create(request.userId, request.uploadedURL)
        val goalGifticon = goalGifticonService.create(
            goalId = request.goalId,
            gifticonId = gifticon.id
        )
        return GoalGifticonResponse(
            goalGifticonId = goalGifticon.id,
            goalId = goal.id,
            gifticonId = gifticon.id,
            gifticonURL = request.uploadedURL
        )
    }

    fun retrieveByGoalId(
        goalId: Long,
        userId: Long
    ): GifticonResponse {
        val goal = goalService.findById(goalId)
        isBrokenTiming(goal)

        val goalGifticon = if (userId != goal.userId) {
            null
        } else {
            goalGifticonService.findByGoalId(goal.id)
        }
        val winnerGifticonId = winnerService.findByGoalIdAndUserId(goalId, userId)?.gifticonId
        val actualGifticonId = goalGifticon?.gifticonId ?: winnerGifticonId ?: throw BaseException.of(
            ExceptionCode.E403_FORBIDDEN,
            "접근할 수 없는 기프티콘입니다."
        )
        val gifticon = gifticonService.findById(actualGifticonId)

        return GifticonResponse(
            goalId = goalId,
            gifticonId = actualGifticonId,
            gifticonURL = gifticon.url.value
        )
    }

    @Transactional
    fun updateGifticonURLByGoalId(request: GoalGifticonUpdateServiceRequest): GoalGifticonResponse {
        val userEntity = userService.findById(request.userId).fromDto()
        val goal = goalService.findById(request.goalId).fromDto(userEntity).toDto()
        val goalGifticon = goalGifticonService.findByGoalId(goal.id)
            ?: throw BaseException.of(ExceptionCode.E404_NOT_FOUND, "다짐에 등록된 기프티콘을 찾을 수 없습니다.")
        val gifticon = gifticonService.findById(goalGifticon.gifticonId)

        validateIsRequestUserHasUpdateAuthority(goal, request.userId)

        gifticon.url = URL(request.gifticonURL)

        return GoalGifticonResponse(
            goalGifticonId = goalGifticon.id,
            goalId = goalGifticon.goalId,
            gifticonId = goalGifticon.gifticonId,
            gifticonURL = gifticon.url.value
        )
    }

    private fun validateIsRequestUserHasUpdateAuthority(
        goal: Goal,
        userId: Long
    ) {
        if (goal.userId != userId) throw BaseException.of(ExceptionCode.E400_BAD_REQUEST)
    }

    private fun isBrokenTiming(goal: Goal) = LocalDateTime.now() > goal.startDate
    private fun isNotBettingTypeBilling(goalType: GoalType) = goalType != GoalType.BILLING
    private fun isBrokenUserGoal(goal: Goal, userId: Long) = goal.userId != userId
}