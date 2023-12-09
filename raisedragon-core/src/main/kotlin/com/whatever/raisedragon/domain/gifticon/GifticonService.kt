package com.whatever.raisedragon.domain.gifticon

import com.whatever.raisedragon.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class GifticonService(
    private val gifticonRepository: GifticonRepository,
    private val userRepository: UserRepository
) {

    fun create(
        userId: Long,
        presignedURL: String,
    ): Gifticon {
        val gifticon = gifticonRepository.save(
            GifticonEntity(
                userEntity = userRepository.findById(userId).get(),
                url = URL(presignedURL)
            )
        )
        return gifticon.toDto()
    }
}