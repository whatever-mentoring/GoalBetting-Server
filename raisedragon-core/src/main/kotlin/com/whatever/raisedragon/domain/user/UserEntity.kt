package com.whatever.raisedragon.domain.user

import com.whatever.raisedragon.domain.BaseEntity
import jakarta.persistence.*

@Table(name = "user")
@Entity
class UserEntity(

    @Column(name = "oauth_token_payload", nullable = true, length = 255)
    val oauthTokenPayload: String?,

    @Column(name = "fcm_token_payload", nullable = true, length = 255)
    val fcmTokenPayload: String?,

    @Embedded
    val nickname: Nickname

) : BaseEntity()

fun User.fromDto(): UserEntity = UserEntity(
    oauthTokenPayload = oauthTokenPayload,
    fcmTokenPayload = fcmTokenPayload,
    nickname = nickname,
)

@Embeddable
data class Nickname(
    @Column(name = "nickname")
    val value: String
) {
    companion object {
        fun generateRandomNickname(): Nickname {
            return Nickname("random nickname")
        }
    }
}

