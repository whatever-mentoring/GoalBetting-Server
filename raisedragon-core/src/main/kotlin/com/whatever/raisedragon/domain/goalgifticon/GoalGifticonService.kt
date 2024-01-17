package com.whatever.raisedragon.domain.goalgifticon

import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.domain.gifticon.GifticonRepository
import com.whatever.raisedragon.domain.goal.Goal
import com.whatever.raisedragon.domain.goal.GoalRepository
import com.whatever.raisedragon.domain.goal.fromDto
import com.whatever.raisedragon.domain.user.UserEntity
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
        gifticonId: Long,
        userId: Long,
    ): GoalGifticon {
        val goalGifticon = goalGifticonRepository.save(
            GoalGifticonEntity(
                goalEntity = goalRepository.findById(goalId).orElseThrow(notFoundExceptionSupplier),
                gifticonEntity = gifticonRepository.findById(gifticonId).orElseThrow(notFoundExceptionSupplier)
            )
        )
        return goalGifticon.toDto()
    }

    fun loadById(id: Long): GoalGifticon {
        return goalGifticonRepository.findById(id).orElseThrow(notFoundExceptionSupplier).toDto()
    }

    fun loadByGoalAndUserEntity(
        goal: Goal,
        userEntity: UserEntity
    ): GoalGifticon? {
        return goalGifticonRepository.findByGoalEntity(goal.fromDto(userEntity))?.toDto()
            ?: throw notFoundExceptionSupplier.get()
    }

    fun findByGoalId(goalId: Long): GoalGifticon? {
        val goalEntity = goalRepository.findById(goalId).orElseThrow(notFoundExceptionSupplier)
        return goalGifticonRepository.findByGoalEntity(goalEntity)?.toDto()
    }
}