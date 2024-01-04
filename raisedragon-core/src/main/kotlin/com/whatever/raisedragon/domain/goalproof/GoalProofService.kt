package com.whatever.raisedragon.domain.goalproof

import com.whatever.raisedragon.domain.gifticon.URL
import com.whatever.raisedragon.domain.goal.Goal
import com.whatever.raisedragon.domain.goal.GoalRepository
import com.whatever.raisedragon.domain.goal.fromDto
import com.whatever.raisedragon.domain.user.User
import com.whatever.raisedragon.domain.user.UserRepository
import com.whatever.raisedragon.domain.user.fromDto
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class GoalProofService(
    private val goalRepository: GoalRepository,
    private val userRepository: UserRepository,
    private val goalProofRepository: GoalProofRepository
) {

    @Transactional
    fun create(
        user: User,
        goal: Goal,
        url: URL,
        comment: Comment
    ): GoalProof {
        val goalProof = goalProofRepository.save(
            GoalProofEntity(
                userEntity = user.fromDto(),
                goalEntity = goal.fromDto(user.fromDto()),
                url = url,
                comment = comment,
            )
        )
        return goalProof.toDto()
    }

    fun existsGoalIdAndDateTimeBetween(goalId: Long, targetDateTime: LocalDateTime): Boolean {
        val todayStartDateTime = LocalDateTime.of(
            targetDateTime.year,
            targetDateTime.month,
            targetDateTime.dayOfMonth,
            0,
            0,
            0
        )
        val todayEndDateTime = LocalDateTime.of(
            targetDateTime.year,
            targetDateTime.month,
            targetDateTime.dayOfMonth,
            23,
            59,
            59
        )
        return goalProofRepository.existsByGoalEntityAndCreatedAtBetween(
            goalEntity = goalRepository.findByIdOrNull(goalId)
                ?: throw throw IllegalArgumentException("cannot find goal $goalId"),
            todayStartDateTime = todayStartDateTime,
            todayEndDateTime = todayEndDateTime
        )
    }

    fun findById(goalProofId: Long): GoalProof? {
        return goalProofRepository.findByIdOrNull(goalProofId)?.toDto()
    }

    fun countAllByGoalId(goalId: Long): Int {
        val goalEntity =
            goalRepository.findByIdOrNull(goalId) ?: throw IllegalArgumentException("cannot find goal $goalId")
        return goalProofRepository.countAllByGoalEntity(goalEntity)
    }

    fun findAllByGoalIdAndUserId(goalId: Long, userId: Long): List<GoalProof> {
        val goalEntity =
            goalRepository.findByIdOrNull(goalId) ?: throw IllegalArgumentException("cannot find goal $goalId")
        val userEntity =
            userRepository.findByIdOrNull(userId) ?: throw IllegalArgumentException("cannot find user $userId")
        return goalProofRepository.findAllByUserEntityAndGoalEntity(goalEntity = goalEntity, userEntity = userEntity)
            .map { it.toDto() }
    }

    @Transactional
    fun update(goalProofId: Long, url: URL? = null, comment: Comment? = null): GoalProof {
        val goalProof = goalProofRepository.findByIdOrNull(goalProofId)
            ?: throw IllegalArgumentException("cannot find goalProof $goalProofId")
        url?.let { goalProof.url = it }
        comment?.let { goalProof.comment = it }
        return goalProof.toDto()
    }

    @Transactional
    fun hardDeleteByUserId(userId: Long) {
        val goalProofs = goalProofRepository.findAllByUserEntity(
            userEntity = userRepository.findByIdOrNull(userId)
                ?: throw IllegalArgumentException("Cannot find user $userId")
        )
        goalProofRepository.deleteAll(goalProofs)
    }
}