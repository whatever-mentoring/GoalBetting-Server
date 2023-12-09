package com.whatever.raisedragon.applicationservice

import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode
import com.whatever.raisedragon.controller.goalproof.GoalProofCreateUpdateResponse
import com.whatever.raisedragon.controller.goalproof.GoalProofListRetrieveResponse
import com.whatever.raisedragon.controller.goalproof.GoalProofRetrieveResponse
import com.whatever.raisedragon.domain.gifticon.URL
import com.whatever.raisedragon.domain.goal.GoalService
import com.whatever.raisedragon.domain.goalproof.Comment
import com.whatever.raisedragon.domain.goalproof.GoalProofService
import com.whatever.raisedragon.domain.user.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GoalProofApplicationService(
    private val goalProofService: GoalProofService,
    private val goalService: GoalService,
    private val userService: UserService,
) {

    @Transactional
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
        return GoalProofCreateUpdateResponse(GoalProofRetrieveResponse.of(goalProof))
    }

    fun retrieve(goalProofId: Long): GoalProofRetrieveResponse {
        val goalProof = goalProofService.findById(goalProofId) ?: throw BaseException.of(ExceptionCode.E404_NOT_FOUND)
        return GoalProofRetrieveResponse.of(goalProof)
    }

    fun retrieveAll(goalId: Long, userId: Long): GoalProofListRetrieveResponse {
        val goalProofs = goalProofService.findAllByGoalIdAndUserId(goalId, userId)
        return GoalProofListRetrieveResponse(goalProofs.map { GoalProofRetrieveResponse.of(it) })
    }
}