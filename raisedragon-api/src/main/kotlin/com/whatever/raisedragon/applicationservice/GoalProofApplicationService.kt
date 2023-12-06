package com.whatever.raisedragon.applicationservice

import com.whatever.raisedragon.controller.goalproof.GoalProofCreateUpdateResponse
import com.whatever.raisedragon.controller.goalproof.GoalProofRetrieveResponse
import com.whatever.raisedragon.domain.goalproof.Document
import com.whatever.raisedragon.domain.goalproof.GoalProofService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class GoalProofApplicationService(
    private val goalProofService: GoalProofService
) {

    fun create(): GoalProofCreateUpdateResponse {
        return GoalProofCreateUpdateResponse(
            GoalProofRetrieveResponse(
                id = 0L,
                userId = 0L,
                goalId = 0L,
                document = Document("Fake Document")
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