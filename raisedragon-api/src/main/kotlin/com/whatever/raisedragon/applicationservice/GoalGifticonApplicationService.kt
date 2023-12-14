package com.whatever.raisedragon.applicationservice

import com.whatever.raisedragon.aws.s3.S3Agent
import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode
import com.whatever.raisedragon.controller.goalgifticon.GoalGifticonResponse
import com.whatever.raisedragon.domain.gifticon.GifticonService
import com.whatever.raisedragon.domain.goal.BettingType
import com.whatever.raisedragon.domain.goal.Goal
import com.whatever.raisedragon.domain.goal.GoalService
import com.whatever.raisedragon.domain.goalgifticon.GoalGifticonService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@Transactional(readOnly = true)
@Service
class GoalGifticonApplicationService(
    private val gifticonService: GifticonService,
    private val goalService: GoalService,
    private val goalGifticonService: GoalGifticonService,
    private val s3Agent: S3Agent
) {

    @Transactional
    fun createAndUploadGifticon(
        userId: Long,
        goalId: Long,
        gifticonFile: MultipartFile
    ): GoalGifticonResponse {
        val goal = goalService.loadById(goalId)
        if (isNotBettingTypeBilling(goal.type)) throw BaseException.of(
            exceptionCode = ExceptionCode.E400_BAD_REQUEST,
            executionMessage = "기프티콘을 업르도하는 중, 무료 다짐에는 기프티콘을 업로드할 수 없습니다."
        )

        if (isBrokenUserGoal(goal, userId)) throw BaseException.of(
            exceptionCode = ExceptionCode.E400_BAD_REQUEST,
            executionMessage = "기프티콘을 업로드하는 중, 요청한 유저가 생성한 다짐에 대한 요청이 아닙니다."
        )

        if (isBrokenTiming(goal)) throw BaseException.of(
            exceptionCode = ExceptionCode.E400_BAD_REQUEST,
            executionMessage = "기프티콘을 업로드하는 중, 이미 다짐 수행이 시작되어 업로드할 수 없습니다."
        )

        val uploadedURL = s3Agent.upload(S3_PREFIX_DIRECTORY, gifticonFile)
        val gifticon = gifticonService.create(userId, uploadedURL)
        val goalGifticon = goalGifticonService.create(
            goalId = goalId,
            gifticonId = gifticon.id,
            userId = userId
        )
        return GoalGifticonResponse(
            goalGifticonId = goalGifticon.id,
            goalId = goal.id,
            gifticonId = gifticon.id,
            gifticonURL = uploadedURL
        )
    }

    private fun isBrokenTiming(goal: Goal) = LocalDateTime.now() > goal.startDate
    private fun isNotBettingTypeBilling(bettingType: BettingType) = bettingType != BettingType.BILLING
    private fun isBrokenUserGoal(goal: Goal, userId: Long) = goal.userId != userId


    companion object {
        private const val S3_PREFIX_DIRECTORY = "gifticon/"
    }
}