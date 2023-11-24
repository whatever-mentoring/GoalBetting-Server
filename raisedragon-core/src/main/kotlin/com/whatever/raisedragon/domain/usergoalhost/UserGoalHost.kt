package com.whatever.raisedragon.domain.usergoalhost

import com.whatever.raisedragon.domain.BaseEntity
import com.whatever.raisedragon.domain.goal.Goal
import com.whatever.raisedragon.domain.user.User
import jakarta.persistence.*

@Table(name = "user_goal_host")
@Entity
class UserGoalHost(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    val goal: Goal,

    ) : BaseEntity()