package com.whatever.raisedragon.domain.goalgifticon

import com.whatever.raisedragon.domain.BaseEntity
import com.whatever.raisedragon.domain.gifticon.GifticonEntity
import com.whatever.raisedragon.domain.goal.GoalEntity
import jakarta.persistence.*

@Table(name = "goal_gifticon")
@Entity
class GoalGifticon(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    val goalEntity: GoalEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gifticon_id")
    val gifticon: GifticonEntity,
) : BaseEntity()