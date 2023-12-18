package com.whatever.raisedragon.domain.goalgifticon

import com.whatever.raisedragon.domain.gifticon.GifticonRepository
import com.whatever.raisedragon.domain.goal.Goal
import com.whatever.raisedragon.domain.goal.GoalRepository
import com.whatever.raisedragon.domain.goal.fromDto
import com.whatever.raisedragon.domain.user.UserEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class GoalGifticonService(
    private val goalGifticonRepository: GoalGifticonRepository,
    private val goalRepository: GoalRepository,
    private val gifticonRepository: GifticonRepository
) {

    fun create(
        goalId: Long,
        gifticonId: Long,
        userId: Long,
    ): GoalGifticon {
        val goalGifticon = goalGifticonRepository.save(
            GoalGifticonEntity(
                goalEntity = goalRepository.findById(goalId).get(),
                gifticonEntity = gifticonRepository.findById(gifticonId).get()
            )
        )
        return goalGifticon.toDto()
    }

    fun loadById(id: Long): GoalGifticon {
        return goalGifticonRepository.findById(id).get().toDto() ?: throw IllegalArgumentException("리소스가 존재하지 않습니다.")
    }

    fun loadByGoalAndUserEntity(
        goal: Goal,
        userEntity: UserEntity
    ): GoalGifticon {
        return goalGifticonRepository.findByGoalEntity(goal.fromDto(userEntity)).toDto()
    }
}