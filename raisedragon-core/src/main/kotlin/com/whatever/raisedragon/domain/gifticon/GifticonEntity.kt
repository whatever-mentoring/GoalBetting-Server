package com.whatever.raisedragon.domain.gifticon

import com.whatever.raisedragon.domain.BaseEntity
import com.whatever.raisedragon.domain.user.UserEntity
import jakarta.persistence.*

@Table(name = "gifticon")
@Entity
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

@Embeddable
data class URL(val url: String)

