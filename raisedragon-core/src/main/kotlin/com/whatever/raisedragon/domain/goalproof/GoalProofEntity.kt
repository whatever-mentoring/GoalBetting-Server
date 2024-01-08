package com.whatever.raisedragon.domain.goalproof

import com.whatever.raisedragon.domain.BaseEntity
import com.whatever.raisedragon.domain.gifticon.URL
import com.whatever.raisedragon.domain.goal.GoalEntity
import com.whatever.raisedragon.domain.user.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction

@Table(name = "goal_proof")
@Entity
@SQLRestriction("deleted_at IS NULL")
class GoalProofEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val userEntity: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    val goalEntity: GoalEntity,

    @Column(name = "url")
    var url: URL,

    @Column(name = "comment")
    var comment: Comment,
) : BaseEntity()

@Embeddable
data class Comment(
    @Column(name = "comment")
    val value: String,
)