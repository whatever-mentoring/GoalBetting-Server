package com.whatever.raisedragon.domain.gifticon

import java.time.LocalDateTime

data class Gifticon(
    val id: Long,
    val userId: Long,
    val url: URL,
    val isValidated: Boolean,
    var deletedAt: LocalDateTime?,
    var createdAt: LocalDateTime?,
    var updatedAt: LocalDateTime?
)

fun GifticonEntity.toDto(): Gifticon = Gifticon(
    id = id,
    userId = userEntity.id,
    url = url,
    isValidated = isValidated,
    deletedAt = deletedAt,
    createdAt = createdAt,
    updatedAt = updatedAt
)