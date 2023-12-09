package com.whatever.raisedragon.applicationservice

import com.whatever.raisedragon.controller.goalproof.GoalProofCreateUpdateResponse
import com.whatever.raisedragon.controller.goalproof.GoalProofRetrieveResponse
import com.whatever.raisedragon.domain.goal.GoalService
import com.whatever.raisedragon.domain.goalproof.Document
import com.whatever.raisedragon.domain.goalproof.GoalProofService
import com.whatever.raisedragon.domain.user.UserService
import com.whatever.raisedragon.security.authentication.UserInfo
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
        document: Document
    ): GoalProofCreateUpdateResponse {
        val goalProof = goalProofService.create(
            user = userService.loadById(userId),
            goal = goalService.loadById(goalId),
            document = document
        )
        return GoalProofCreateUpdateResponse(
            GoalProofRetrieveResponse(
                id = goalProof.id,
                userId = goalProof.userId,
                goalId = goalProof.goalId,
                document = document
            )
        )
    }

    fun retrieve(): GoalProofRetrieveResponse {
        return GoalProofRetrieveResponse(
            id = 0L,
            userId = 0L,
            goalId = 0L,
            document = Document("Fake Document")
        )
    }
}