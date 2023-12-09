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
import java.lang.IllegalArgumentException

@Service
@Transactional
class GoalProofService(
    private val goalRepository: GoalRepository,
    private val userRepository: UserRepository,
    private val goalProofRepository: GoalProofRepository
) {

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

    fun findById(goalProofId: Long): GoalProof? {
        return goalProofRepository.findByIdOrNull(goalProofId)?.toDto()
    }

    fun findAllByGoalIdAndUserId(goalId: Long, userId: Long): List<GoalProof> {
        val goalEntity = goalRepository.findByIdOrNull(goalId) ?: throw IllegalArgumentException()
        val userEntity = userRepository.findByIdOrNull(userId) ?: throw IllegalArgumentException()
        return goalProofRepository.findAllByUserEntityAndGoalEntity(goalEntity = goalEntity, userEntity = userEntity)
            .map { it.toDto() }
    }
}