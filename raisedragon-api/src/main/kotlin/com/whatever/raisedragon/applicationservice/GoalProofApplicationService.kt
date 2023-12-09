package com.whatever.raisedragon.applicationservice

import com.whatever.raisedragon.controller.goalproof.GoalProofCreateUpdateResponse
import com.whatever.raisedragon.controller.goalproof.GoalProofRetrieveResponse
import com.whatever.raisedragon.domain.gifticon.URL
import com.whatever.raisedragon.domain.goal.GoalService
import com.whatever.raisedragon.domain.goalproof.Comment
import com.whatever.raisedragon.domain.goalproof.GoalProofService
import com.whatever.raisedragon.domain.user.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class GoalProofApplicationService(
    private val goalProofService: GoalProofService,
    private val goalService: GoalService,
    private val userService: UserService,
) {

    fun create(
        userId: Long,
        goalId: Long,
        url: String,
        comment: String
    ): GoalProofCreateUpdateResponse {
        val goalProof = goalProofService.create(
            user = userService.loadById(userId),
            goal = goalService.loadById(goalId),
            url = URL(url),
            comment = Comment(comment)
        )
        return GoalProofCreateUpdateResponse(
            GoalProofRetrieveResponse(
                id = goalProof.id,
                userId = goalProof.userId,
                goalId = goalProof.goalId,
                url = goalProof.url,
                comment = goalProof.comment
            )
        )
    }

    fun retrieve(): GoalProofRetrieveResponse {
        return GoalProofRetrieveResponse(
            id = 0L,
            userId = 0L,
            goalId = 0L,
            url = URL("goalProof.url"),
            comment = Comment("goalProof.comment")
        )
    }
}