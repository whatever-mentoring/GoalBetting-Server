package com.whatever.raisedragon.domain.betting

import com.whatever.raisedragon.domain.BaseEntity
import com.whatever.raisedragon.domain.goal.Goal
import com.whatever.raisedragon.domain.user.UserEntity
import jakarta.persistence.*

@Table(name = "betting")
@Entity
class BettingEntity(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    val goal: Goal,

    @Enumerated(EnumType.STRING)
    val predictionType: PredictionType,

    @Enumerated(EnumType.STRING)
    val result: Result

) : BaseEntity()

enum class PredictionType {
    SUCCESS, FAIL
}

enum class Result {
    PROCEEDING, SUCCESS, FAIL
}