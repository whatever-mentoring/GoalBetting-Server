package com.whatever.raisedragon.domain.goalgifticon

import com.whatever.raisedragon.domain.BaseEntity
import com.whatever.raisedragon.domain.gifticon.GifticonEntity
import com.whatever.raisedragon.domain.goal.GoalEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction

@Table(name = "goal_gifticon")
@Entity
@SQLRestriction("deleted_at IS NULL")
class GoalGifticonEntity(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    val goalEntity: GoalEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gifticon_id")
    val gifticonEntity: GifticonEntity,
) : BaseEntity()