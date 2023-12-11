package com.whatever.raisedragon.domain.goal

import com.whatever.raisedragon.domain.user.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional
@Service
class GoalService(
    private val goalRepository: GoalRepository,
    private val userRepository: UserRepository
) {

    fun create(
        userId: Long,
        content: Content,
        bettingType: BettingType,
        threshold: Threshold,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): Goal {
        val goal = goalRepository.save(
            GoalEntity(
                userEntity = userRepository.findById(userId).get(),
                type = bettingType,
                content = content,
                threshold = threshold,
                result = Result.PROCEEDING,
                startDate = startDate,
                endDate = endDate
            )
        )
        return goal.toDto()
    }

    @Transactional(readOnly = true)
    fun loadById(id: Long): Goal {
        return goalRepository.findByIdOrNull(id)?.toDto() ?: throw Exception()
    }

    @Transactional(readOnly = true)
    fun loadAllByUserId(userId: Long): List<GoalEntity> {
        return goalRepository.findAllByUserEntity(
            userRepository.findById(userId).get()
        )
    }

    @Transactional(readOnly = true)
    fun loadByGoalIdAndUserId(goalId: Long, userId: Long): GoalEntity? {
        return goalRepository.findByIdAndUserEntity(
            id = goalId,
            userEntity = userRepository.findById(userId).get()
        )
    }
}