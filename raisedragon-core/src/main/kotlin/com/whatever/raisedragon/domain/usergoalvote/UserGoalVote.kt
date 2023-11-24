package com.whatever.raisedragon.domain.usergoalvote

import com.whatever.raisedragon.domain.BaseEntity
import com.whatever.raisedragon.domain.goal.Goal
import com.whatever.raisedragon.domain.user.User
import jakarta.persistence.*

@Table(name = "user_goal_vote")
@Entity
class UserGoalVote(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    val goal: Goal,

    @Enumerated(EnumType.STRING)
    val type: Type,

    @Enumerated(EnumType.STRING)
    val result: Result,

    ) : BaseEntity()

enum class Type {
    SUCCESS, FAIL
}

enum class Result {
    SUCCESS, FAIL
}