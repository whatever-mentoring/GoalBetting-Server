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

    @Enumerated(EnumType.STRING)
    val result: Result,

    @Column(name = "start_date", nullable = false)
    val startDate: LocalDateTime,

    @Column(name = "end_date", nullable = false)
    val endDate: LocalDateTime

) : BaseEntity()

fun Goal.fromDto(): GoalEntity = GoalEntity(
    type = type,
    content = content,
    threshold = threshold,
    result = result,
    startDate = startDate,
    endDate = endDate,
)

enum class BettingType {
    FREE, BILLING
}

@Embeddable
data class Content(
    @Column(name = "content")
    val value: String
)

enum class Result {
    PROCEEDING, SUCCESS, FAIL
}

@Embeddable
data class Threshold(
    @Column(name = "threshold")
    val value: Int
)