package com.whatever.raisedragon.domain.betting

import com.whatever.raisedragon.domain.BaseEntity
import com.whatever.raisedragon.domain.goal.GoalEntity
import com.whatever.raisedragon.domain.user.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction

@Table(name = "betting")
@Entity
@SQLRestriction("deleted_at IS NULL")
class BettingEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val userEntity: UserEntity, // 배팅을 건 사람이다.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    val goalEntity: GoalEntity,

    @Enumerated(EnumType.STRING)
    @Column(name = "betting_prediction_type", nullable = false)
    var bettingPredictionType: BettingPredictionType,

    @Enumerated(EnumType.STRING)
    @Column(name = "betting_result", nullable = false)
    var bettingResult: BettingResult,

    ) : BaseEntity()

enum class BettingPredictionType {
    SUCCESS, FAIL
}

enum class BettingResult {
    PROCEEDING, FAIL, GET_GIFTICON, NO_GIFTICON
}
