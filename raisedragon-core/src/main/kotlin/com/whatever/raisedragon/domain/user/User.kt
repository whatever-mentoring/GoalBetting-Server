package com.whatever.raisedragon.domain.user

import com.whatever.raisedragon.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "user")
@Entity
class User(

    @Column(name = "unique_id", nullable = true, length = 255)
    val oauthTokenPayload: String?,

    @Column(name = "unique_id", nullable = true, length = 255)
    val fcmTokenPayload: String?

) : BaseEntity()