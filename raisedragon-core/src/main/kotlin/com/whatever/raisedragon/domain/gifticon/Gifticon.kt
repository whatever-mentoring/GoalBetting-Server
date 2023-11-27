package com.whatever.raisedragon.domain.gifticon

import com.whatever.raisedragon.domain.BaseEntity
import jakarta.persistence.*

@Table(name = "gifticon")
@Entity
class Gifticon(

    @Embedded
    @Column(name = "url", nullable = true, length = 255)
    val url: URL?,

    @Column(name = "is_validated")
    var isValidated: Boolean = true

) : BaseEntity()

@Embeddable
data class URL(val url: String)

