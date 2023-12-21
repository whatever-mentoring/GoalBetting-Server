package com.whatever.raisedragon.domain.goal

import com.whatever.raisedragon.domain.user.UserEntity
import com.whatever.raisedragon.domain.user.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional(readOnly = true)
@Service
class GoalService(
    private val goalRepository: GoalRepository,
    private val userRepository: UserRepository
) {

    @Transactional
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

    fun loadById(id: Long): Goal {
        return goalRepository.findByIdOrNull(id)?.toDto()
            ?: throw IllegalArgumentException("다짐을 조회하는 중, 잘못된 내용을 요청하셨습니다.")
    }

    fun loadAllByUserId(userId: Long): List<Goal> {
        return goalRepository.findAllByUserEntity(
            userRepository.findById(userId).get()
        ).map { it.toDto() }
    }

    fun findAllByIds(goalIds: Set<Long>): List<Goal> {
        return goalRepository.findAllById(goalIds).map { it.toDto() }
    }

    @Transactional
    fun modify(
        goal: Goal,
        userEntity: UserEntity,
        content: String,
    ): Goal {
        val goalEntity = goal.fromDto(userEntity)
        goalEntity.content = Content(content)

        return goalEntity.toDto()
    }

    @Transactional
    fun delete(
        goal: Goal,
        userEntity: UserEntity,
    ): Goal {
        val goalEntity = goal.fromDto(userEntity)
        goalEntity.deletedAt = LocalDateTime.now()

        return goalEntity.toDto()
    }

    fun increaseThreshold(goal: Goal, userEntity: UserEntity) {
        val goalEntity = goal.fromDto(userEntity)
        goalEntity.threshold = Threshold(goalEntity.threshold.value + 1)
    }
}