package com.whatever.raisedragon.domain.goalgifticon

import com.whatever.raisedragon.domain.gifticon.Gifticon
import com.whatever.raisedragon.domain.gifticon.fromDto
import com.whatever.raisedragon.domain.goal.Goal
import com.whatever.raisedragon.domain.goal.fromDto
import com.whatever.raisedragon.domain.user.User
import com.whatever.raisedragon.domain.user.fromDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class GoalGifticonService(
    private val goalGifticonRepository: GoalGifticonRepository
) {

    fun create(
        goal: Goal,
        gifticon: Gifticon,
        user: User,
    ) {
        goalGifticonRepository.save(
            GoalGifticonEntity(
                goal.fromDto(),
                gifticon.fromDto(user.fromDto())
            )
        )
    }
}