package com.whatever.raisedragon.applicationservice

import com.whatever.raisedragon.applicationservice.dto.GoalProofResponse
import com.whatever.raisedragon.applicationservice.dto.UserResponse
import com.whatever.raisedragon.controller.goalproof.GoalProofCreateUpdateResponse
import com.whatever.raisedragon.controller.goalproof.GoalProofRetrieveResponse
import com.whatever.raisedragon.domain.goal.BettingType
import com.whatever.raisedragon.domain.goal.Content
import com.whatever.raisedragon.domain.goal.Goal
import com.whatever.raisedragon.domain.goal.Threshold
import com.whatever.raisedragon.domain.goalproof.Document
import com.whatever.raisedragon.domain.goalproof.GoalProof
import com.whatever.raisedragon.domain.goalproof.GoalProofService
import com.whatever.raisedragon.domain.user.Nickname
import com.whatever.raisedragon.domain.user.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class GoalProofApplicationService(
    private val goalProofService: GoalProofService
) {

    fun create(): GoalProofCreateUpdateResponse {
        return GoalProofCreateUpdateResponse(
            GoalProofResponse.of(
                GoalProof(
                    0L,
                    User(
                        0L,
                        "sample",
                        "sample",
                        Nickname("sample"),
                        null,
                        null,
                        null
                    ),
                    goal = Goal(
                        type = BettingType.BILLING,
                        content = Content("sample"),
                        threshold = Threshold(0),
                        deadline = LocalDateTime.now()
                    ),
                    document = Document("sample"),
                    null,
                    null,
                    null
                )
            )
        )
    }

    fun retrieve(): GoalProofRetrieveResponse {
        return GoalProofRetrieveResponse(
            user = UserResponse.of(
                User(
                    0L,
                    "sample",
                    "sample",
                    Nickname("sample"),
                    null,
                    null,
                    null
                )
            ),
            goal = Goal(
                type = BettingType.BILLING,
                content = Content("sample"),
                threshold = Threshold(0),
                deadline = LocalDateTime.now()
            ),
            document = Document("sample")
        )
    }
}