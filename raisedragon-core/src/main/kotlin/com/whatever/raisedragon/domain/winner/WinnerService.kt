package com.whatever.raisedragon.domain.winner

import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.domain.gifticon.GifticonRepository
import com.whatever.raisedragon.domain.goal.GoalRepository
import com.whatever.raisedragon.domain.user.Nickname
import com.whatever.raisedragon.domain.user.UserRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.Supplier

@Service
@Transactional(readOnly = true)
class WinnerService(
    @Qualifier("notFoundExceptionSupplier") private val notFoundExceptionSupplier: Supplier<BaseException>,
    private val winnerRepository: WinnerRepository,
    private val goalRepository: GoalRepository,
    private val userRepository: UserRepository,
    private val gifticonRepository: GifticonRepository
) {

    @Transactional
    fun create(goalId: Long, userId: Long, gifticonId: Long): Winner {
        val goalEntity =
            goalRepository.findById(goalId).orElseThrow(notFoundExceptionSupplier)
        val userEntity =
            userRepository.findById(userId).orElseThrow(notFoundExceptionSupplier)
        val gifticonEntity = gifticonRepository.findById(gifticonId).orElseThrow(notFoundExceptionSupplier)
        val winnerEntity = WinnerEntity(goalEntity, userEntity, gifticonEntity)
        winnerRepository.save(winnerEntity)
        return winnerEntity.toDto()
    }

    fun findByGoalIdAndUserId(goalId: Long, userId: Long): Winner? {
        val goalEntity = goalRepository.findById(goalId).orElseThrow(notFoundExceptionSupplier)
        val userEntity = userRepository.findById(userId).orElseThrow(notFoundExceptionSupplier)
        return winnerRepository.findByGoalEntityAndUserEntity(goalEntity, userEntity)?.toDto()
    }

    fun findWinnerNicknameByGoalId(goalId: Long): Nickname? {
        return winnerRepository
            .findByGoalEntity(goalRepository.findById(goalId).orElseThrow(notFoundExceptionSupplier))
            ?.userEntity?.nickname
    }
}