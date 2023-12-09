package com.whatever.raisedragon.domain.goalproof

import com.whatever.raisedragon.domain.gifticon.URL
import com.whatever.raisedragon.domain.goal.Goal
import com.whatever.raisedragon.domain.goal.fromDto
import com.whatever.raisedragon.domain.user.User
import com.whatever.raisedragon.domain.user.fromDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class GoalProofService(
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
                goalEntity = goal.fromDto(),
                url = url,
                comment = comment,
            )
        )
        return goalProof.toDto()
    }
}