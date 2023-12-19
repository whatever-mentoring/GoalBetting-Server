package com.whatever.raisedragon.domain.goal

import com.whatever.raisedragon.domain.BaseEntity
import com.whatever.raisedragon.domain.user.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDateTime

@Table(name = "goal")
@Entity
@SQLRestriction("deleted_at IS NULL")
class GoalEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val userEntity: UserEntity,

    @Enumerated(EnumType.STRING)
    val type: BettingType,

    @Embedded
    @Column(name = "content", nullable = false, length = 255)
    var content: Content,

    @Embedded
    @Column(name = "threshold", nullable = false)
    var threshold: Threshold = Threshold(0),

    @Enumerated(EnumType.STRING)
    val result: Result,

    @Column(name = "start_date", nullable = false)
    val startDate: LocalDateTime,

    @Column(name = "end_date", nullable = false)
    val endDate: LocalDateTime

) : BaseEntity()

fun Goal.fromDto(userEntity: UserEntity): GoalEntity = GoalEntity(
    id = id,
    userEntity = userEntity,
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

enum class Result {
    PROCEEDING, SUCCESS, FAIL
}

@Embeddable
data class Content(
    @Column(name = "content")
    val value: String
)

@Embeddable
data class Threshold(
    @Column(name = "threshold")
    val value: Int
)