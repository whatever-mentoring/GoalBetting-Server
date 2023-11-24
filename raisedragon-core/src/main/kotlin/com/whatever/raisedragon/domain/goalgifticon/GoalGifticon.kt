package com.whatever.raisedragon.domain.goalgifticon

import com.whatever.raisedragon.domain.BaseEntity
import com.whatever.raisedragon.domain.gifticon.Gifticon
import com.whatever.raisedragon.domain.goal.Goal
import jakarta.persistence.*

@Table(name = "goal_gifticon")
@Entity
class GoalGifticon(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    val goal: Goal,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gifticon_id")
    val gifticon: Gifticon,
) : BaseEntity()