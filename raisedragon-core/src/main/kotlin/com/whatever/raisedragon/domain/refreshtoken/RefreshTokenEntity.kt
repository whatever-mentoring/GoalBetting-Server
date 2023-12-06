package com.whatever.raisedragon.domain.refreshtoken

import com.whatever.raisedragon.domain.BaseEntity
import com.whatever.raisedragon.domain.user.UserEntity
import jakarta.persistence.*

@Table(name = "refresh_token")
@Entity
class RefreshTokenEntity(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val userEntity: UserEntity,

    @Column(name = "payload", nullable = true, length = 255)
    val payload: String?

) : BaseEntity()
