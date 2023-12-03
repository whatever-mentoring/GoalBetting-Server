package com.whatever.raisedragon.domain.goalproof

import com.whatever.raisedragon.domain.BaseEntity
import com.whatever.raisedragon.domain.goal.Goal
import com.whatever.raisedragon.domain.user.UserEntity
import jakarta.persistence.*

@Table(name = "goal_proof")
@Entity
class GoalProofEntity(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    val goal: Goal,

    @Embedded
    val document: Document

) : BaseEntity()

@Embeddable
data class Document(val document: String)