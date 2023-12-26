package com.whatever.raisedragon.domain.winner

import com.whatever.raisedragon.domain.gifticon.GifticonRepository
import com.whatever.raisedragon.domain.goal.GoalRepository
import com.whatever.raisedragon.domain.user.Nickname
import com.whatever.raisedragon.domain.user.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class WinnerService(
    private val winnerRepository: WinnerRepository,
    private val goalRepository: GoalRepository,
    private val userRepository: UserRepository,
    private val gifticonRepository: GifticonRepository
) {

    @Transactional
    fun create(goalId: Long, userId: Long, gifticonId: Long): Winner {
        val goalEntity =
            goalRepository.findByIdOrNull(goalId) ?: throw IllegalStateException("cannot find goal $goalId")
        val userEntity =
            userRepository.findByIdOrNull(userId) ?: throw IllegalStateException("cannot find user $userId")
        val gifticonEntity = gifticonRepository.findByIdOrNull(gifticonId)
            ?: throw IllegalStateException("cannot find gifticon $gifticonId")
        val winnerEntity = WinnerEntity(goalEntity, userEntity, gifticonEntity)
        winnerRepository.save(winnerEntity)
        return winnerEntity.toDto()
    }

    fun findByGoalIdAndUserId(goalId: Long, userId: Long): Winner? {
        val goalEntity =
            goalRepository.findByIdOrNull(goalId) ?: throw IllegalStateException("cannot find goal $goalId")
        val userEntity =
            userRepository.findByIdOrNull(userId) ?: throw IllegalStateException("cannot find user $userId")
        return winnerRepository.findByGoalEntityAndUserEntity(goalEntity, userEntity)?.toDto()
    }

    fun findWinnerNicknameByGoalId(goalId: Long): Nickname? {
        return winnerRepository
            .findByGoalEntity(goalRepository.findByIdOrNull(goalId)
                ?: throw IllegalStateException("cannot find goal $goalId"))
            ?.userEntity?.nickname
    }
}