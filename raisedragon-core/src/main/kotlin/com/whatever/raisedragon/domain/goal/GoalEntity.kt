package com.whatever.raisedragon.domain.goal

import com.whatever.raisedragon.domain.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Table(name = "goal")
@Entity
class GoalEntity(

    @Enumerated(EnumType.STRING)
    val type: BettingType,

    @Embedded
    @Column(name = "content", nullable = false, length = 255)
    val content: Content,

    @Embedded
    @Column(name = "threshold", nullable = false)
    val threshold: Threshold = Threshold(0),

    @Column
    val deadline: LocalDateTime

) : BaseEntity()

enum class BettingType {
    FREE, BILLING
}

@Embeddable
data class Content(val content: String)

@Embeddable
data class Threshold(val threshold: Int)