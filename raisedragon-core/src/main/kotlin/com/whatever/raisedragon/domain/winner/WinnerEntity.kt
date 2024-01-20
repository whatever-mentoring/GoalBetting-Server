package com.whatever.raisedragon.domain.winner

import com.whatever.raisedragon.domain.BaseEntity
import com.whatever.raisedragon.domain.gifticon.GifticonEntity
import com.whatever.raisedragon.domain.goal.GoalEntity
import com.whatever.raisedragon.domain.user.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction

@Table(name = "winner")
@Entity
@SQLRestriction("deleted_at IS NULL")
class WinnerEntity(
    @OneToOne
    @JoinColumn(name = "goal_id")
    val goalEntity: GoalEntity,

    @OneToOne
    @JoinColumn(name = "user_id")
    val userEntity: UserEntity,

    @OneToOne
    @JoinColumn(name = "gifticon_id")
    val gifticonEntity: GifticonEntity,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
) : BaseEntity()