package com.whatever.raisedragon.domain.goalproof

import com.whatever.raisedragon.domain.BaseEntity
import com.whatever.raisedragon.domain.goal.GoalEntity
import com.whatever.raisedragon.domain.user.UserEntity
import jakarta.persistence.*

@Table(name = "goal_proof")
@Entity
class GoalProofEntity(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val userEntity: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    val goalEntity: GoalEntity,

    @Embedded
    val document: Document

) : BaseEntity()

@Embeddable
data class Document(
    @Column(name = "document")
    val value: String
)