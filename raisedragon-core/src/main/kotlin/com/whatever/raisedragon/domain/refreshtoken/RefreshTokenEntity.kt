package com.whatever.raisedragon.domain.refreshtoken

import com.whatever.raisedragon.domain.BaseEntity
import com.whatever.raisedragon.domain.user.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction

@Table(name = "refresh_token")
@Entity
@SQLRestriction("deleted_at IS NULL")
class RefreshTokenEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val userEntity: UserEntity,

    @Column(name = "payload", nullable = true, length = 255)
    val payload: String?

) : BaseEntity()

fun RefreshToken.fromDto(userEntity: UserEntity): RefreshTokenEntity = RefreshTokenEntity(
    userEntity = userEntity,
    payload = payload,
)