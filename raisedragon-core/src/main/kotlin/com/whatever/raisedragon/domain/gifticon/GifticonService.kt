package com.whatever.raisedragon.domain.gifticon

import com.whatever.raisedragon.domain.user.User
import com.whatever.raisedragon.domain.user.fromDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class GifticonService(
    private val gifticonRepository: GifticonRepository,
) {

    fun create(
        user: User,
        presignedURL: String,
    ): Gifticon {
        val gifticon = gifticonRepository.save(
            GifticonEntity(
                userEntity = user.fromDto(),
                url = URL(presignedURL)
            )
        )
        return gifticon.toDto()
    }
}