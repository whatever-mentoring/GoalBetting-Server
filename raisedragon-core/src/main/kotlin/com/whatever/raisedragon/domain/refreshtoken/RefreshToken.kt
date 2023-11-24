package com.whatever.raisedragon.domain.refreshtoken

import com.whatever.raisedragon.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "refresh_token")
@Entity
class RefreshToken(

    @Embedded
    @Column(name = "payload", nullable = true, length = 255)
    val payload: String

) : BaseEntity() {

    fun generatePayload() {

    }

    fun refreshPayload() {

    }

    fun validate() {

    }

    fun isExpired() {

    }
}
