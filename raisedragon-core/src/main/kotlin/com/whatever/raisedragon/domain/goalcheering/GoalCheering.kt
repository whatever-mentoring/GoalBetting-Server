package com.whatever.raisedragon.domain.goalcheering

import com.whatever.raisedragon.domain.BaseEntity
import com.whatever.raisedragon.domain.goal.Goal
import com.whatever.raisedragon.domain.user.User
import jakarta.persistence.*

@Table(name = "goal_cheering")
@Entity
class GoalCheering(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    val goal: Goal,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @Embedded
    val cheeringMessage: CheeringMessage

) : BaseEntity()

@Embeddable
data class CheeringMessage(val cheeringMessage: String)