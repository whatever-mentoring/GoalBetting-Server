package com.whatever.raisedragon.domain.gifticon

import com.whatever.raisedragon.domain.BaseEntity
import com.whatever.raisedragon.domain.user.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction

@Table(name = "gifticon")
@Entity
@SQLRestriction("deleted_at IS NULL")
class GifticonEntity(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val userEntity: UserEntity,

    @Embedded
    @Column(name = "url", nullable = false, length = 255)
    val url: URL,

    @Column(name = "is_validated")
    var isValidated: Boolean = true

) : BaseEntity()

fun Gifticon.fromDto(userEntity: UserEntity): GifticonEntity = GifticonEntity(
    userEntity = userEntity,
    url = url,
    isValidated = isValidated,
)

@Embeddable
data class URL(
    @Column(name = "url")
    val value: String
)

