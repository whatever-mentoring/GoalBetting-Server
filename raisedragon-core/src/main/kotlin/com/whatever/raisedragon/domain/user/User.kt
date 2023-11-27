package com.whatever.raisedragon.domain.user

import com.whatever.raisedragon.domain.BaseEntity
import jakarta.persistence.*

@Table(name = "user")
@Entity
class User(

    @Column(name = "oauth_token_payload", nullable = true, length = 255)
    val oauthTokenPayload: String?,

    @Column(name = "fcm_token_payload", nullable = true, length = 255)
    val fcmTokenPayload: String?,

    @Embedded
    val nickname: Nickname

) : BaseEntity()

@Embeddable
data class Nickname(val nickname: String)