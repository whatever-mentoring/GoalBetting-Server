package com.whatever.raisedragon.applicationservice

import com.whatever.raisedragon.domain.gifticon.GifticonService
import com.whatever.raisedragon.domain.goal.*
import com.whatever.raisedragon.domain.goalgifticon.GoalGifticonService
import com.whatever.raisedragon.domain.user.UserService
import com.whatever.raisedragon.security.authentication.UserInfo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional
@Service
class GoalApplicationService(
    private val goalService: GoalService,
    private val gifticonService: GifticonService,
    private val goalGifticonService: GoalGifticonService,
    private val userService: UserService,
) {

    fun createGoal(
        content: Content,
        bettingType: BettingType,
        threshold: Threshold,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        userId: Long,
        presignedURL: String
    ): Goal {
        val user = userService.loadById(userId)
        if (bettingType == BettingType.BILLING) {
            val goal = goalService.create(
                content = content,
                bettingType = bettingType,
                threshold = threshold,
                startDate = startDate,
                endDate = endDate
            )
            val gifticon = gifticonService.create(
                user = user,
                presignedURL = presignedURL
            )
            goalGifticonService.create(
                goal = goal,
                gifticon = gifticon,
                user = user
            )
            return goal
        }

        return goalService.create(
            content = content,
            bettingType = bettingType,
            threshold = threshold,
            startDate = startDate,
            endDate = endDate
        )
    }
}