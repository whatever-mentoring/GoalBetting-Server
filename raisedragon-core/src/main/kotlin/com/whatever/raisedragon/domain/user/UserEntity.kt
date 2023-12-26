package com.whatever.raisedragon.domain.user

import com.whatever.raisedragon.domain.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDateTime

@Table(name = "user")
@Entity
@SQLRestriction("deleted_at IS NULL")
class UserEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "oauth_token_payload", nullable = true, length = 255)
    var oauthTokenPayload: String?,

    @Column(name = "fcm_token_payload", nullable = true, length = 255)
    var fcmTokenPayload: String?,

    @Embedded
    var nickname: Nickname

) : BaseEntity()

fun User.fromDto(): UserEntity = UserEntity(
    id = id!!,
    oauthTokenPayload = oauthTokenPayload,
    fcmTokenPayload = fcmTokenPayload,
    nickname = nickname,
)

fun UserEntity.disable() {
    this.deletedAt = LocalDateTime.now()
}

@Embeddable
data class Nickname(
    @Column(name = "nickname")
    val value: String
) {
    companion object {
        fun generateRandomNickname(): Nickname {
            val randomAdjective = RandomWordsNickname.adjectives.random()
            val randomNoun = RandomWordsNickname.nouns.random()
            return Nickname("$randomAdjective $randomNoun")
        }
    }
}
