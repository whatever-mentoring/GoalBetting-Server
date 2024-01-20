package com.whatever.raisedragon.domain.goalgifticon

import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.domain.gifticon.GifticonRepository
import com.whatever.raisedragon.domain.goal.GoalRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.Supplier

@Transactional(readOnly = true)
@Service
class GoalGifticonService(
    @Qualifier("notFoundExceptionSupplier") private val notFoundExceptionSupplier: Supplier<BaseException>,
    private val goalGifticonRepository: GoalGifticonRepository,
    private val goalRepository: GoalRepository,
    private val gifticonRepository: GifticonRepository
) {

    @Transactional
    fun create(
        goalId: Long,
        gifticonId: Long
    ): GoalGifticon {
        val goalGifticon = goalGifticonRepository.save(
            GoalGifticonEntity(
                goalEntity = goalRepository.findById(goalId).orElseThrow(notFoundExceptionSupplier),
                gifticonEntity = gifticonRepository.findById(gifticonId).orElseThrow(notFoundExceptionSupplier)
            )
        )
        return goalGifticon.toDto()
    }

    fun findByGoalId(goalId: Long): GoalGifticon? {
        val goalEntity = goalRepository.findById(goalId).orElseThrow(notFoundExceptionSupplier)
        return goalGifticonRepository.findByGoalEntity(goalEntity)?.toDto()
    }
}