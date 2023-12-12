package com.whatever.raisedragon.domain.goalcheering

import com.whatever.raisedragon.domain.BaseEntity
import com.whatever.raisedragon.domain.goal.GoalEntity
import com.whatever.raisedragon.domain.user.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction

@Table(name = "goal_cheering")
@Entity
@SQLRestriction("deleted_at IS NULL")
class GoalCheeringEntity(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    val goalEntity: GoalEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val userEntity: UserEntity,

    @Embedded
    val cheeringMessage: CheeringMessage

) : BaseEntity()

@Embeddable
data class CheeringMessage(val cheeringMessage: String)