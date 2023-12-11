package com.whatever.raisedragon.domain.goalgifticon

import com.whatever.raisedragon.domain.gifticon.GifticonRepository
import com.whatever.raisedragon.domain.goal.GoalRepository
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
}