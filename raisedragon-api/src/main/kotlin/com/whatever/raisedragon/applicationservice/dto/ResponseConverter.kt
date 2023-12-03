package com.whatever.raisedragon.applicationservice.dto

import com.whatever.raisedragon.domain.betting.Betting
import com.whatever.raisedragon.domain.betting.PredictionType
import com.whatever.raisedragon.domain.betting.Result
import com.whatever.raisedragon.domain.goal.Goal
import com.whatever.raisedragon.domain.goalproof.Document
import com.whatever.raisedragon.domain.goalproof.GoalProof
import com.whatever.raisedragon.domain.user.Nickname
import com.whatever.raisedragon.domain.user.User
import java.time.LocalDateTime

data class UserResponse(
    val oauthTokenPayload: String?,
    val fcmTokenPayload: String?,
    val nickname: Nickname,
    var deletedAt: LocalDateTime?,
    var createdAt: LocalDateTime?,
    var updatedAt: LocalDateTime?
) {
    companion object {
        fun of(user: User): UserResponse {
            return UserResponse(
                oauthTokenPayload = user.oauthTokenPayload,
                fcmTokenPayload = user.fcmTokenPayload,
                nickname = user.nickname,
                deletedAt = user.deletedAt,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt
            )
        }
    }
}

data class BettingResponse(
    val user: User,
    val goal: Goal,
    val predictionType: PredictionType,
    val result: Result,
    var deletedAt: LocalDateTime?,
    var createdAt: LocalDateTime?,
    var updatedAt: LocalDateTime?
) {
    companion object {
        fun of(betting: Betting): BettingResponse {
            return BettingResponse(
                user = betting.user,
                goal = betting.goal,
                predictionType = betting.predictionType,
                result = betting.result,
                deletedAt = betting.deletedAt,
                createdAt = betting.createdAt,
                updatedAt = betting.updatedAt
            )
        }
    }
}

data class GoalProofResponse(
    val user: User,
    val goal: Goal,
    val document: Document,
    var deletedAt: LocalDateTime?,
    var createdAt: LocalDateTime?,
    var updatedAt: LocalDateTime?
) {
    companion object {
        fun of(goalProof: GoalProof): GoalProofResponse {
            return GoalProofResponse(
                user = goalProof.user,
                goal = goalProof.goal,
                document = goalProof.document,
                deletedAt = goalProof.deletedAt,
                createdAt = goalProof.createdAt,
                updatedAt = goalProof.updatedAt
            )
        }
    }
}