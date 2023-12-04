package com.whatever.raisedragon.applicationservice

import com.whatever.raisedragon.applicationservice.dto.GoalProofResponse
import com.whatever.raisedragon.applicationservice.dto.UserResponse
import com.whatever.raisedragon.controller.goalproof.GoalProofCreateUpdateResponse
import com.whatever.raisedragon.controller.goalproof.GoalProofRetrieveResponse
import com.whatever.raisedragon.domain.goal.*
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
                    id = 0L,
                    user = User(
                        id = 0L,
                        oauthTokenPayload = "sample",
                        fcmTokenPayload = "sample",
                        nickname = Nickname("sample"),
                        deletedAt = null,
                        createdAt = null,
                        updatedAt = null
                    ),
                    goal = Goal(
                        id = 1L,
                        type = BettingType.BILLING,
                        content = Content("sample"),
                        threshold = Threshold(0),
                        startDate = LocalDateTime.now().plusDays(1),
                        endDate = LocalDateTime.now().plusMonths(1),
                        createdAt = LocalDateTime.now(),
                        updatedAt = LocalDateTime.now(),
                        deletedAt = null
                    ),
                    document = Document("sample"),
                    deletedAt = null,
                    createdAt = null,
                    updatedAt = null
                )
            )
        )
    }

    fun retrieve(): GoalProofRetrieveResponse {
        return GoalProofRetrieveResponse(
            user = UserResponse.of(
                User(
                    id = 0L,
                    oauthTokenPayload = "sample",
                    fcmTokenPayload = "sample",
                    nickname = Nickname("sample"),
                    deletedAt = null,
                    createdAt = null,
                    updatedAt = null
                )
            ),
            goal = Goal(
                id = 1L,
                type = BettingType.BILLING,
                content = Content("sample"),
                threshold = Threshold(0),
                startDate = LocalDateTime.now().plusDays(1),
                endDate = LocalDateTime.now().plusMonths(1),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                deletedAt = null
            ),
            document = Document("sample")
        )
    }
}